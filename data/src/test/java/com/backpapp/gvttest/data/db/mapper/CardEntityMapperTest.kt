package com.backpapp.gvttest.data.db.mapper

import com.backpapp.details.test.MainCoroutineListener
import com.backpapp.details.test.TestBehaviorSpec
import com.backpapp.gvttest.data.db.CardEntityType
import com.backpapp.gvttest.data.db.exception.MalformedDbException
import com.backpapp.gvttest.data.factory.CardEntityFactory
import com.backpapp.gvttest.data.factory.CardFactory
import com.backpapp.gvttest.domain.model.Card
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class CardEntityMapperTest : TestBehaviorSpec({

    val mainCoroutineListener = MainCoroutineListener()
    listeners(mainCoroutineListener)
    val mapper = CardEntityMapper()

    Given("card is blue") {
        val card = CardFactory.blueCard()

        When("mapToEntity") {
            val entity = mapper.mapToEntity(card)

            Then("entity id is card id") {
                entity.id shouldBe card.id
            }
            And("entity title is card title") {
                entity.title shouldBe card.title
            }
            And("entity name is card name") {
                entity.name shouldBe card.name
            }
            And("entity number is card number") {
                entity.number shouldBe card.number
            }
            And("entity type is blue") {
                entity.type shouldBe CardEntityType.BLUE
            }
            And("firstName is null") {
                entity.firstName shouldBe null
            }
            And("surname is null") {
                entity.surname shouldBe null
            }
            And("referenceNumber is null") {
                entity.referenceNumber shouldBe null
            }
            And("expiryDateAet is null") {
                entity.expiryDateAet shouldBe null
            }
        }
    }

    Given("card is green") {
        val card = CardFactory.greenCard()

        When("mapToEntity") {
            val entity = mapper.mapToEntity(card)

            Then("entity id is card id") {
                entity.id shouldBe card.id
            }
            And("entity title is card title") {
                entity.title shouldBe card.title
            }
            And("entity name is card name") {
                entity.name shouldBe card.name
            }
            And("entity number is card number") {
                entity.number shouldBe card.number
            }
            And("entity type is green") {
                entity.type shouldBe CardEntityType.GREEN
            }
            And("firstName is null") {
                entity.firstName shouldBe null
            }
            And("surname is null") {
                entity.surname shouldBe null
            }
            And("referenceNumber is null") {
                entity.referenceNumber shouldBe null
            }
            And("expiryDateAet is 10-11-24") {
                entity.expiryDateAet shouldBe "10-11-24"
            }
        }
    }

    Given("card is purple") {
        val card = CardFactory.purpleCard()

        When("mapToEntity") {
            val entity = mapper.mapToEntity(card)

            Then("entity id is card id") {
                entity.id shouldBe card.id
            }
            And("entity title is card title") {
                entity.title shouldBe card.title
            }
            And("entity name null") {
                entity.name shouldBe null
            }
            And("entity number is card number") {
                entity.number shouldBe card.number
            }
            And("entity type is purple") {
                entity.type shouldBe CardEntityType.PURPLE
            }
            And("firstName is null") {
                entity.firstName shouldBe "firstName"
            }
            And("surname is null") {
                entity.surname shouldBe "surname"
            }
            And("referenceNumber is card reference number") {
                entity.referenceNumber shouldBe card.referenceNumber
            }
            And("expiryDateAet is null") {
                entity.expiryDateAet shouldBe "10-11-24"
            }
        }
    }

    Given("a correct entity with green type") {
        val entity = CardEntityFactory.createEntity(type = CardEntityType.GREEN)

        When("mapTo") {
            val card = mapper.mapToDomain(entity)

            Then("card id is entity id") {
                card.shouldBeInstanceOf<Card.Green>()
                card.id shouldBe entity.id
            }
            And("card title is entity title") {
                card.shouldBeInstanceOf<Card.Green>()
                card.title shouldBe entity.title
            }
            And("card name is entity name") {
                card.shouldBeInstanceOf<Card.Green>()
                card.name shouldBe entity.name
            }
            And("card number is entity number") {
                card.shouldBeInstanceOf<Card.Green>()
                card.number shouldBe entity.number
            }
            And("card expiry date is entity expiry date") {
                card.shouldBeInstanceOf<Card.Green>()
                card.expiryDate shouldBe LocalDate.of(2024, 11, 10)
            }
        }
    }

    Given("a malformed entity with green type and name null") {
        val entity = CardEntityFactory.createEntity(name = null, type = CardEntityType.GREEN)

        When("mapTo") {
            Then("throw MalformedDbException") {
                assertThrows<MalformedDbException> {
                    mapper.mapToDomain(entity)
                }
            }
        }
    }

    Given("a correct entity with purple type") {
        val entity = CardEntityFactory.createEntity(type = CardEntityType.PURPLE)

        When("mapTo") {
            val card = mapper.mapToDomain(entity)

            Then("card id is entity id") {
                card.shouldBeInstanceOf<Card.Purple>()
                card.id shouldBe entity.id
            }
            And("card title is entity title") {
                card.shouldBeInstanceOf<Card.Purple>()
                card.title shouldBe entity.title
            }
            And("card firstName is entity firstName") {
                card.shouldBeInstanceOf<Card.Purple>()
                card.firstName shouldBe entity.firstName
            }
            And("card surname is entity surname") {
                card.shouldBeInstanceOf<Card.Purple>()
                card.surname shouldBe entity.surname
            }
            And("card expiry date is entity expiry date") {
                card.shouldBeInstanceOf<Card.Purple>()
                card.expiryDate shouldBe LocalDate.of(2024, 11, 10)
            }
            And("card number is entity number") {
                card.shouldBeInstanceOf<Card.Purple>()
                card.number shouldBe entity.number
            }
            And("card reference number is entity  reference number") {
                card.shouldBeInstanceOf<Card.Purple>()
                card.referenceNumber shouldBe entity.referenceNumber
            }
        }
    }

    Given("a malformed entity with purple type and first name null") {
        val entity = CardEntityFactory.createEntity(firstName = null, type = CardEntityType.PURPLE)

        When("mapTo") {
            Then("throw MalformedDbException") {
                assertThrows<MalformedDbException> {
                    mapper.mapToDomain(entity)
                }
            }
        }
    }

    Given("a malformed entity with purple type and last name null") {
        val entity = CardEntityFactory.createEntity(surname = null, type = CardEntityType.PURPLE)

        When("mapTo") {
            Then("throw MalformedDbException") {
                assertThrows<MalformedDbException> {
                    mapper.mapToDomain(entity)
                }
            }
        }
    }

    Given("a malformed entity with purple type and expiry date null") {
        val entity =
            CardEntityFactory.createEntity(expiryDateAet = null, type = CardEntityType.PURPLE)

        When("mapTo") {
            Then("throw MalformedDbException") {
                assertThrows<MalformedDbException> {
                    mapper.mapToDomain(entity)
                }
            }
        }
    }

    Given("a malformed entity with purple type and referenceNumber null") {
        val entity =
            CardEntityFactory.createEntity(referenceNumber = null, type = CardEntityType.PURPLE)

        When("mapTo") {
            Then("throw MalformedDbException") {
                assertThrows<MalformedDbException> {
                    mapper.mapToDomain(entity)
                }
            }
        }
    }

    Given("a correct entity with blue type") {
        val entity = CardEntityFactory.createEntity(type = CardEntityType.BLUE)

        When("mapTo") {
            val card = mapper.mapToDomain(entity)

            Then("card id is entity id") {
                card.shouldBeInstanceOf<Card.Blue>()
                card.id shouldBe entity.id
            }
            And("card title is entity title") {
                card.shouldBeInstanceOf<Card.Blue>()
                card.title shouldBe entity.title
            }
            And("card anem is entity name") {
                card.shouldBeInstanceOf<Card.Blue>()
                card.name shouldBe entity.name
            }
            And("card number is entity number") {
                card.shouldBeInstanceOf<Card.Blue>()
                card.number shouldBe entity.number
            }
        }
    }

    Given("a malformed entity with blue type and name null") {
        val entity = CardEntityFactory.createEntity(name = null, type = CardEntityType.BLUE)

        When("mapTo") {
            Then("throw MalformedDbException") {
                assertThrows<MalformedDbException> {
                    mapper.mapToDomain(entity)
                }
            }
        }
    }
})