package com.backpapp.gvttest.data.api

import app.cash.turbine.test
import com.backpapp.details.test.MainCoroutineListener
import com.backpapp.details.test.TestBehaviorSpec
import com.backpapp.gvttest.data.api.mapper.CardApiResponseMapper
import com.backpapp.gvttest.data.api.model.CardApiResponse
import com.backpapp.gvttest.data.db.CardDao
import com.backpapp.gvttest.data.db.CardEntity
import com.backpapp.gvttest.data.db.exception.MalformedDbException
import com.backpapp.gvttest.data.db.mapper.CardEntityMapper
import com.backpapp.gvttest.data.factory.CardApiResponseFactory
import com.backpapp.gvttest.data.factory.CardFactory
import com.backpapp.gvttest.domain.model.Card
import com.backpapp.gvttest.domain.repository.Response
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals

class RealCardRepositoryTest : TestBehaviorSpec({

    val mainCoroutineListener = MainCoroutineListener()
    listeners(mainCoroutineListener)

    val cachedEntity: CardEntity = mockk()
    val cachedEntities: List<CardEntity> = listOf(cachedEntity)
    val cachedCard: Card = CardFactory.blueCard()
    val cachedCards: List<Card> = listOf(cachedCard)
    val apiRespons: CardApiResponse = CardApiResponseFactory.createBlue()
    val apiResponse: List<CardApiResponse> = listOf(apiRespons)
    val apiCard: Card = CardFactory.blueCard()
    val apiCards: List<Card> = listOf(apiCard)

    val cardApi: CardApi = mockk()
    val dao: CardDao = mockk()
    val responseMapper: CardApiResponseMapper = mockk()
    val dbMapper: CardEntityMapper = mockk()

    val repository = RealCardRepository(cardApi, dao, responseMapper, dbMapper)

    Given("cached list is correctly cached and not empty and api returns success") {
        When("getCards with cache enabled") {
            coEvery { dao.getAll() } returns cachedEntities
            every { dbMapper.mapToDomain(cachedEntities) } returns cachedCards
            coEvery { cardApi.getCards() } returns apiResponse
            every { responseMapper.mapToDomain(apiResponse) } returns apiCards
            every { dbMapper.mapToEntity(apiCards) } returns cachedEntities

            val result = repository.getCards(refresh = false)

            Then("result is cached cards") {
                result.test {
                    assertEquals(Response.Success(cachedCards), awaitItem())
                    awaitComplete()
                }
            }
        }

        When("getCards with cache disabled") {
            coEvery { dao.getAll() } returns cachedEntities
            coEvery { cardApi.getCards() } returns apiResponse
            every { responseMapper.mapToDomain(apiResponse) } returns apiCards
            every { dbMapper.mapToEntity(apiCards) } returns cachedEntities

            val result = repository.getCards(refresh = true)

            Then("result is api cards") {
                result.test {
                    assertEquals(Response.Success(apiCards), awaitItem())
                    awaitComplete()
                }
            }
        }

        When("getCard with cache enabled") {
            coEvery { dao.get("cardId") } returns cachedEntity
            coEvery { cardApi.getCard("cardId") } returns apiRespons
            every { responseMapper.mapToDomain(apiRespons) } returns apiCard
            every { dbMapper.mapToEntity(apiCard) } returns cachedEntity

            val result = repository.getCard("cardId", refresh = false)

            Then("result is cached cards") {
                result.test {
                    assertEquals(Response.Success(cachedCard), awaitItem())
                    awaitComplete()
                }
            }
        }

        When("getCard with cache disabled") {
            coEvery { dao.get("cardId") } returns cachedEntity
            coEvery { cardApi.getCard("cardId") } returns CardApiResponseFactory.createBlue()
            every { responseMapper.mapToDomain(apiRespons) } returns apiCard
            every { dbMapper.mapToEntity(apiCard) } returns cachedEntity

            val result = repository.getCard("cardId", refresh = true)

            Then("result is api cards") {
                result.test {
                    assertEquals(Response.Success(apiCard), awaitItem())
                    awaitComplete()
                }
            }
        }
    }

    Given("cached list is empty and api returns success") {

        When("getCards with cache enabled") {
            coEvery { dao.getAll() } returns cachedEntities
            every { dbMapper.mapToDomain(cachedEntities) } returns listOf()
            coEvery { cardApi.getCards() } returns apiResponse
            every { responseMapper.mapToDomain(apiResponse) } returns apiCards
            every { dbMapper.mapToEntity(apiCards) } returns cachedEntities

            val result = repository.getCards(refresh = false)

            Then("result is api cards") {
                result.test {
                    assertEquals(Response.Success(apiCards), awaitItem())
                    awaitComplete()
                }
            }
        }

        When("getCard with cache enabled") {

            coEvery { cardApi.getCard("cardId") } returns apiRespons
            every { responseMapper.mapToDomain(apiRespons) } returns apiCard
            every { dbMapper.mapToEntity(apiCards) } returns cachedEntities

            val result = repository.getCard(cardId = "cardId", refresh = false)

            Then("result is api cards") {
                result.test {
                    assertEquals(Response.Success(apiCard), awaitItem())
                    awaitComplete()
                }
            }
        }
    }

    Given("cached list is not correctly cached and api returns success") {
        When("getCards with cache enabled") {
            coEvery { dao.getAll() } returns cachedEntities
            every { dbMapper.mapToDomain(cachedEntities) } throws MalformedDbException
            coEvery { cardApi.getCards() } returns apiResponse
            every { responseMapper.mapToDomain(apiResponse) } returns apiCards

            val result = repository.getCards(refresh = false)

            Then("result is api cards") {
                result.test {
                    assertEquals(Response.Success(apiCards), awaitItem())
                    awaitComplete()
                }
            }
        }

        When("getCard with cache enabled") {
            every { dbMapper.mapToDomain(cachedEntities) } throws MalformedDbException
            coEvery { cardApi.getCard("cardId") } returns apiRespons
            every { responseMapper.mapToDomain(apiRespons) } returns apiCard

            val result = repository.getCard(cardId = "cardId", refresh = false)

            Then("result is api cards") {
                result.test {
                    assertEquals(Response.Success(apiCard), awaitItem())
                    awaitComplete()
                }
            }
        }
    }

    Given("cached list is correctly cached and not empty and api returns error") {
        When("getCards with cache disabled") {
            val e = Exception()
            every { dbMapper.mapToDomain(cachedEntities) } returns cachedCards
            coEvery { cardApi.getCards() } throws e

            val result = repository.getCards(refresh = true)

            Then("result is error") {
                result.test {
                    assertEquals(Response.Error<List<Card>>(e), awaitItem())
                    awaitComplete()
                }
            }
        }

        When("getCard with cache disabled") {
            val e = Exception()
            every { dbMapper.mapToDomain(cachedEntities) } returns cachedCards
            coEvery { cardApi.getCard("cardId") } throws e

            val result = repository.getCard(cardId = "cardId", refresh = true)

            Then("result is error") {
                result.test {
                    assertEquals(Response.Error<List<Card>>(e), awaitItem())
                    awaitComplete()
                }
            }
        }
    }

    Given("cached list is empty and api returns error") {
        When("getCards with cache enabled") {
            val e = Exception()
            coEvery { dao.getAll() } returns cachedEntities
            every { dbMapper.mapToDomain(cachedEntities) } returns listOf()
            coEvery { cardApi.getCards() } throws e

            val result = repository.getCards(refresh = false)

            Then("result is api error") {
                result.test {
                    assertEquals(Response.Error<List<Card>>(e), awaitItem())
                    awaitComplete()
                }
            }
        }

        When("getCard with cache enabled") {
            val e = Exception()
            every { dbMapper.mapToDomain(cachedEntities) } returns listOf()
            coEvery { cardApi.getCard("cardId") } throws e

            val result = repository.getCard(cardId = "cardId", refresh = false)

            Then("result is api error") {
                result.test {
                    assertEquals(Response.Error<List<Card>>(e), awaitItem())
                    awaitComplete()
                }
            }
        }
    }

    Given("cached list is not correctly cached and api returns error") {

        When("getCards with cache enabled") {
            val e = Exception()
            coEvery { dao.getAll() } returns cachedEntities
            every { dbMapper.mapToDomain(cachedEntities) } throws MalformedDbException
            coEvery { cardApi.getCards() } throws e

            val result = repository.getCards(refresh = false)

            Then("result is api error") {
                result.test {
                    assertEquals(Response.Error<List<Card>>(e), awaitItem())
                    awaitComplete()
                }
            }
        }

        When("getCard with cache enabled") {
            val e = Exception()
            coEvery { dao.getAll() } returns cachedEntities
            every { dbMapper.mapToDomain(cachedEntities) } throws MalformedDbException
            coEvery { cardApi.getCard("cardId") } throws e


            val result = repository.getCard(cardId = "cardId", refresh = false)

            Then("result is api error") {
                result.test {
                    assertEquals(Response.Error<List<Card>>(e), awaitItem())
                    awaitComplete()
                }
            }
        }
    }
})

