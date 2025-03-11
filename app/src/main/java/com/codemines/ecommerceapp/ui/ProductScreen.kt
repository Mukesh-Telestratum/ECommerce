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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var isDescriptionExpanded by remember { mutableStateOf(false) } // Track expansion

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
                            painter = painterResource(id = heartIcon), // Reference the heart.png file
                            contentDescription = "Heart Icon",
                            modifier = Modifier.size(24.dp) // Set size to 24dp
                        )
                    }
                    val shareText = "Check out this amazing product: https://klinq.com/"

                    IconButton(onClick = { shareContent(context, shareText) }) {

                        Image(
                            painter = painterResource(id = R.drawable.share), // Reference the heart.png file
                            contentDescription = "Heart Icon",
                            modifier = Modifier.size(24.dp) // Set size to 24dp
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
                    // Swipeable Image Pager
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

                // Dots Indicator (With Increased Spacing)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally) // Increase spacing
                ) {
                    repeat(productItem.images.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (pagerState.currentPage == index) 10.dp else 8.dp) // Active dot larger
                                .clip(CircleShape)
                                .background(if (pagerState.currentPage == index) Color.Black else Color(0xFFD3B98A))
                        )
                    }
                }}

                item {
                    // Product Name & Price
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


                    // Color Options
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
                        // 🟢 Quantity Selector (Above Product Description)
                        QuantitySelector()

                        Spacer(modifier = Modifier.height(8.dp))
                        // Row for "Product Description" text and icon
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
                                modifier = Modifier.weight(1f) // This ensures the text takes up available space
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

                        // Animated description visibility
                        AnimatedVisibility(visible = isDescriptionExpanded) {
                            StyledHtmlText(productItem.description) // Call the function here

                        }
                    }
                }


                item {
                    // Buttons
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        val context = LocalContext.current  // Get context for Toast

                        Button(
                            onClick = {            Toast.makeText(context, "Added to Bag!", Toast.LENGTH_SHORT).show() },
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
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground) // ✅ Dynamic border color
                        ) {
                            Text("Share", color = MaterialTheme.colorScheme.onBackground) // ✅ Dynamic text color
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
    val context = LocalContext.current
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb() // ✅ Get the color in @Composable scope
    var spannedText by remember { mutableStateOf<Spanned?>(null) }

    // Convert HTML to Spanned (for Android TextView)
    LaunchedEffect(htmlText) {
        spannedText = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { ctx ->
            TextView(ctx).apply {
                textSize = 14f
                setPadding(16, 8, 16, 8)
                setTextColor(textColor) // ✅ Use the color retrieved earlier
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
    var quantity by remember { mutableStateOf(1) } // Default quantity = 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        // 📝 Quantity Text (Left-aligned)
        Text(
            text = "Quantity",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 🔢 Quantity Selector (Row Layout for Buttons & Number)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Adds space between elements
            modifier = Modifier.width(180.dp) // Fixed width for consistent sizing
        ) {
            // 🔽 Decrease Button (-) | Grey Background
            Box(
                modifier = Modifier
                    .size(50.dp) // Fixed size for consistency
                    .background(Color.Gray, RoundedCornerShape(4.dp))
                    .clickable { if (quantity > 1) quantity-- },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "-", fontSize = 20.sp, color = Color.White)
            }

            // 🔢 Quantity Display (White Background)
            Box(
                modifier = Modifier
                    .size(50.dp) // Same size as buttons
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = quantity.toString(), fontSize = 18.sp, color = Color.Black)
            }

            // 🔼 Increase Button (+) | Black Background
            Box(
                modifier = Modifier
                    .size(50.dp) // Same size as other boxes
                    .background(Color.Black, RoundedCornerShape(4.dp))
                    .clickable { quantity++ },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

