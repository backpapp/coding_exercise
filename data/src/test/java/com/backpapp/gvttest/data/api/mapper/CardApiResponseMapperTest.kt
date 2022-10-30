package com.backpapp.gvttest.data.api.mapper

import com.backpapp.details.test.MainCoroutineListener
import com.backpapp.details.test.TestBehaviorSpec
import com.backpapp.gvttest.data.factory.CardApiResponseFactory
import com.backpapp.gvttest.domain.model.Card
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDate

class CardApiResponseMapperTest : TestBehaviorSpec({

    val mainCoroutineListener = MainCoroutineListener()
    listeners(mainCoroutineListener)
    val mapper = CardApiResponseMapper()

    Given("cardApiResponse is blue") {
        val response = CardApiResponseFactory.createBlue()

        When("mapTo") {
            val result = mapper.mapToDomain(response) as Card.Blue

            Then("result id is response id") {
                assertEquals(response.id, result.id)
            }
            And("result title is response title") {
                assertEquals(response.title, result.title)
            }
            And("result name is response name") {
                assertEquals(response.name, result.name)
            }
            And("result number is response number") {
                assertEquals(response.number, result.number)
            }
        }
    }

    Given("cardApiResponse is green") {
        val response = CardApiResponseFactory.createGreen()

        When("mapTo") {
            val result = mapper.mapToDomain(response) as Card.Green

            Then("result id is response id") {
                assertEquals(response.id, result.id)
            }
            And("result title is response title") {
                assertEquals(response.title, result.title)
            }
            And("result name is response name") {
                assertEquals(response.name, result.name)
            }
            And("result number is response number") {
                assertEquals(response.number, result.number)
            }
            And("result expiryDate is expiry date ") {
                val localDate = LocalDate.of(2024, 10, 11)
                assertEquals(localDate, result.expiryDate)
            }
        }
    }

    Given("cardApiResponse is purple") {
        val response = CardApiResponseFactory.createPurple()

        When("mapTo") {
            val result = mapper.mapToDomain(response) as Card.Purple

            Then("result id is response id") {
                assertEquals(response.id, result.id)
            }
            And("result title is response title") {
                assertEquals(response.title, result.title)
            }
            And("result first name is response first name") {
                assertEquals(response.firstName, result.firstName)
            }
            And("result surname is response surname") {
                assertEquals(response.surname, result.surname)
            }
            And("result number is response number") {
                assertEquals(response.number, result.number)
            }
            And("result expiryDate is expiry date ") {
                val localDate = LocalDate.of(2024, 10, 11)
                assertEquals(localDate, result.expiryDate)
            }
        }
    }
})