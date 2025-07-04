package com.example.practiceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.practiceapp.data.DataItem
import com.example.practiceapp.domain.UserViewModel
import com.example.practiceapp.ui.theme.PracticeAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            PracticeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VideoPlayerApp(viewModel, modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun VideoPlayerApp(viewModel: UserViewModel = UserViewModel(), modifier: Modifier = Modifier) {

    val users by viewModel.userState.collectAsState()
    val isLoading by viewModel.loading.collectAsState()

    Box (modifier = modifier){
//        if (isLoading) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//        } else {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                TopSlider()
                SectionTitle("Recommended")
                HorizontalCardList(users)

                //        SectionTitle("Trending Now")
                //        HorizontalCardList()
            }
//        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopSlider() {
    val imageList = listOf(
        "https://picsum.photos/300/200?1",
        "https://picsum.photos/300/200?2",
        "https://picsum.photos/300/200?3",
        "https://picsum.photos/300/200?4",
        "https://picsum.photos/300/200?5"
    )
    val pagerState = rememberPagerState(pageCount = { imageList.size })

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            AsyncImage(
                model = imageList[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().background(Color.LightGray).clip(
                    RoundedCornerShape(16.dp)).border(5.dp, Color.Gray, RoundedCornerShape(16.dp))
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(imageList.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size( 20.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.DarkGray else Color.LightGray)
                )
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }
        }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun HorizontalCardList(users: List<DataItem>) {
    val imageUrls = listOf(
        "https://picsum.photos/300/200?1",
        "https://picsum.photos/300/200?2",
        "https://picsum.photos/300/200?3",
        "https://picsum.photos/300/200?4",
        "https://picsum.photos/300/200?5"
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(imageUrls) { imageUrl ->
            Card(
                modifier = Modifier
                    .width(160.dp)
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = if (!users.isEmpty())users[0].first_name else "loading",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true,)
@Composable
fun GreetingPreview() {
    PracticeAppTheme {
        VideoPlayerApp()
    }
}