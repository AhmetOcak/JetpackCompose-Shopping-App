package com.ahmetocak.shoppingapp.presentation.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme

@Composable
fun ShoppingCreditCard(
    cardHolderName: String,
    cardNumber: String,
    cardExpiryDate: String,
    cvc: String,
    rotated: Boolean,
    onCardClick: () -> Unit
) {
    val cardType = when (findCreditCardType(cardNumber)) {
        CardType.VISA -> {
            painterResource(id = R.drawable.visa)
        }

        CardType.MASTERCARD -> {
            painterResource(id = R.drawable.mastercard)
        }

        CardType.AMERICAN_EXPRESS -> {
            painterResource(id = R.drawable.american_express)
        }

        CardType.DISCOVER -> {
            painterResource(id = R.drawable.discover)
        }

        else -> {
            painterResource(id = R.drawable.credit_card)
        }
    }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500),
        label = "rotation"
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(500),
        label = "animateFront"
    )

    val animateBack by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(500),
        label = "animateBack"
    )

    Card(
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .clickable {
                onCardClick()
            },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(contentColor = Color.White),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            colorResource(id = R.color.blue_100),
                            colorResource(id = R.color.blue_200),
                            colorResource(id = R.color.blue_300),
                            colorResource(id = R.color.blue_400),
                        )
                    )
                )
        ) {
            if (!rotated) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Icon(
                            painter = painterResource(R.drawable.ic_contactless),
                            contentDescription = "test",
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .padding(top = 6.dp, bottom = 6.dp, end = 20.dp)
                                .graphicsLayer {
                                    alpha = animateFront
                                },
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painter = cardType,
                            contentDescription = null,
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .graphicsLayer {
                                    alpha = animateFront
                                }
                        )
                    }

                    Text(
                        text = cardNumber.replace("....".toRegex(), "$0 "),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .graphicsLayer {
                                alpha = animateFront
                            },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                    )

                    Row(
                        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.two_level_margin)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = stringResource(id = R.string.card_holder),
                                color = Color.DarkGray,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .graphicsLayer {
                                        alpha = animateFront
                                    }
                            )
                            Text(
                                text = cardHolderName,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .graphicsLayer {
                                        alpha = animateFront
                                    }
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = stringResource(id = R.string.valid_thru),
                                color = Color.DarkGray,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .graphicsLayer {
                                        alpha = animateFront
                                    }
                            )
                            Text(
                                text = cardExpiryDate.chunked(2).joinToString(separator = "/"),
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .graphicsLayer {
                                        alpha = animateFront
                                    }
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.padding(top = 20.dp),
                ) {
                    Divider(
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = animateBack
                            },
                        thickness = 50.dp,
                        color = Color.Black
                    )
                    Text(
                        text = cvc,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(10.dp)
                            .background(Color.White)
                            .fillMaxWidth()
                            .graphicsLayer {
                                alpha = animateBack
                                rotationY = rotation
                            }
                            .padding(10.dp),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

private fun findCreditCardType(cardNumber: String): CardType {
    return when {
        cardNumber.startsWith("4") && (cardNumber.length == 13 || cardNumber.length == 16 || cardNumber.length == 19) -> CardType.VISA
        cardNumber.startsWith("5") && (cardNumber.length == 16 || cardNumber.length == 19) -> CardType.MASTERCARD
        (cardNumber.startsWith("34") || cardNumber.startsWith("37")) && (cardNumber.length == 15) -> CardType.AMERICAN_EXPRESS
        cardNumber.startsWith("6") && (cardNumber.length == 16 || cardNumber.length == 19) -> CardType.DISCOVER
        else -> CardType.UNKNOWN
    }
}

enum class CardType {
    VISA,
    MASTERCARD,
    AMERICAN_EXPRESS,
    DISCOVER,
    UNKNOWN
}

@Preview
@Composable
private fun ShoppingCreditCardPreview() {
    ShoppingAppTheme {
        Surface {
            ShoppingCreditCard(
                cardHolderName = "Ahmet Ocak",
                cardNumber = "5425233430109903",
                cardExpiryDate = "0827",
                cvc = "111",
                rotated = false,
                onCardClick = {}
            )
        }
    }
}
@Preview(name = "rotated")
@Composable
private fun ShoppingRotatedCreditCardPreview() {
    ShoppingAppTheme {
        Surface {
            ShoppingCreditCard(
                cardHolderName = "Ahmet Ocak",
                cardNumber = "5425233430109903",
                cardExpiryDate = "0827",
                cvc = "111",
                rotated = true,
                onCardClick = {}
            )
        }
    }
}