package com.codemines.ecommerceapp.ui

import android.content.Context
import android.content.Intent
import android.text.Spanned
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.rememberAsyncImagePainter
import com.codemines.ecommerceapp.R
import com.codemines.ecommerceapp.viewmodel.ProductViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(viewModel: ProductViewModel, onBackClick: () -> Unit) {
    val product by viewModel.product.collectAsState()
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchProductDetails()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Once Collection Weekly", style = TextStyle(fontSize = 20.sp)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    val context = LocalContext.current

                    IconButton(onClick = { Toast.makeText(context, "Added to wishlist!", Toast.LENGTH_SHORT).show()}) {
                        val isDarkMode = isSystemInDarkTheme()
                        val heartIcon = if (isDarkMode) R.drawable.wheart else R.drawable.heart

                        Image(
                            painter = painterResource(id = heartIcon),
                            contentDescription = "Heart Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    val shareText = "Check out this amazing product: https://klinq.com/"

                    IconButton(onClick = { shareContent(context, shareText) }) {

                        Image(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "Heart Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = { Toast.makeText(context, "Added to bag!", Toast.LENGTH_SHORT).show()}) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.additem), contentDescription = "Add to Bag")
                    }
                }
            )
        }
    ) { paddingValues ->
        product?.let { productItem ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    val pagerState = rememberPagerState(pageCount = { productItem.images.size })
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) { page ->
                        Image(
                            painter = rememberAsyncImagePainter(productItem.images[page]),
                            contentDescription = productItem.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    repeat(productItem.images.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(if (pagerState.currentPage == index) Color.Black else Color(0xFFD3B98A))
                        )
                    }
                }}

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = productItem.brand_name,
                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${productItem.price} KWD",
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                    }

                    // Product Details
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Once Collection Weekly",
                            style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                        )
                        Text(
                            text = "SKU: ${productItem.sku}",
                            style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                        )
                    }
                }


                item {


                    Text(
                        text = "Color:",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    LazyRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(productItem.images) { imageUrl ->
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = "Color Option",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Black, CircleShape)
                                    .clickable { }
                            )
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        QuantitySelector()

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "Product Description",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(onClick = { isDescriptionExpanded = !isDescriptionExpanded }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(
                                        if (isDescriptionExpanded) R.drawable.close else R.drawable.open
                                    ),
                                    contentDescription = if (isDescriptionExpanded) "Collapse" else "Expand"
                                )
                            }
                        }

                        AnimatedVisibility(visible = isDescriptionExpanded) {
                            StyledHtmlText(productItem.description)

                        }
                    }
                }


                item {
                    // Buttons
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        val context = LocalContext.current

                        Button(
                            onClick = { Toast.makeText(context, "Added to Bag!", Toast.LENGTH_SHORT).show() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add to bag", color = Color.Black)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        val shareText = "Check out this amazing product: https://klinq.com/"


                        OutlinedButton(
                            onClick = { shareContent(context,shareText) },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
                        ) {
                            Text("Share", color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

fun shareContent(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }

    val chooser = Intent.createChooser(intent, "Share via")
    context.startActivity(chooser)
}
@Composable
fun StyledHtmlText(htmlText: String) {
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()
    var spannedText by remember { mutableStateOf<Spanned?>(null) }

    LaunchedEffect(htmlText) {
        spannedText = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { ctx ->
            TextView(ctx).apply {
                textSize = 14f
                setPadding(16, 8, 16, 8)
                setTextColor(textColor)
            }
        },
        update = { textView ->
            spannedText?.let {
                textView.text = it
            }
        }
    )
}



@Composable
fun QuantitySelector() {
    var quantity by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = "Quantity",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.width(180.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Gray, RoundedCornerShape(4.dp))
                    .clickable { if (quantity > 1) quantity-- },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "-", fontSize = 20.sp, color = Color.White)
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = quantity.toString(), fontSize = 18.sp, color = Color.Black)
            }


            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Black, RoundedCornerShape(4.dp))
                    .clickable { quantity++ },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

