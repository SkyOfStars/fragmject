package com.example.fragment.project.ui.main.navigation

import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fragment.library.base.compose.FullScreenLoading
import com.example.fragment.library.base.compose.TabBar
import com.example.fragment.project.R
import com.example.fragment.project.bean.TreeBean
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavScreen(
    tree: List<TreeBean>,
    onNavigateToSystem: (cid: String) -> Unit = {},
    onNavigateToWeb: (url: String) -> Unit = {},
) {
    val tabs = listOf("导航", "体系")
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    Column {
        TabBar(
            pagerState = pagerState,
            data = tabs,
            textMapping = { it },
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it)
                }
            },
        )
        HorizontalPager(
            count = tabs.size,
            state = pagerState,
        ) { page ->
            if (page == 0) {
                NavLinkContent(onNavigateToWeb = onNavigateToWeb)
            } else if (page == 1) {
                NavSystemContent(
                    tree = tree,
                    onNavigateToSystem = onNavigateToSystem
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NavLinkContent(
    viewModel: NavViewModel = viewModel(),
    onNavigateToWeb: (url: String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    if (uiState.isLoading) {
        FullScreenLoading()
    } else {
        Row {
            LazyColumn(
                modifier = Modifier.width(150.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                itemsIndexed(uiState.navigationResult.toList()) { index, item ->
                    Box(
                        modifier = Modifier
                            .background(
                                colorResource(if (item.isSelected) R.color.white else R.color.gray)
                            )
                            .fillMaxWidth()
                            .height(45.dp)
                            .clickable {
                                viewModel.updateSelectNavigation(index)
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = item.name,
                            color = colorResource(id = R.color.text_333),
                            fontSize = 16.sp,
                        )
                    }
                }
            }
            FlowRow(
                modifier = Modifier
                    .background(colorResource(R.color.white))
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                uiState.articlesResult.forEach {
                    Box(modifier = Modifier.padding(5.dp, 0.dp, 5.dp, 0.dp)) {
                        Button(
                            onClick = {
                                onNavigateToWeb(Uri.encode(it.link))
                            },
                            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(R.color.gray_e5),
                                contentColor = colorResource(R.color.text_666)
                            ),
                            contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 0.dp)
                        ) {
                            Text(
                                text = it.title,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun NavSystemContent(
    tree: List<TreeBean>,
    onNavigateToSystem: (cid: String) -> Unit = {},
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        tree.forEach {
            stickyHeader {
                Text(
                    text = it.name,
                    modifier = Modifier
                        .background(colorResource(R.color.gray))
                        .fillMaxWidth()
                        .padding(15.dp, 5.dp, 15.dp, 5.dp),
                    color = colorResource(R.color.text_666),
                    fontSize = 13.sp
                )
            }
            it.children?.let { children ->
                item {
                    FlowRow(
                        modifier = Modifier
                            .background(colorResource(R.color.gray))
                            .fillMaxWidth()
                    ) {
                        children.forEach { children ->
                            Box(modifier = Modifier.padding(5.dp, 0.dp, 5.dp, 0.dp)) {
                                Button(
                                    onClick = {
                                        onNavigateToSystem(children.id)
                                    },
                                    elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = colorResource(R.color.gray_e5),
                                        contentColor = colorResource(R.color.text_666)
                                    ),
                                    contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 0.dp)
                                ) {
                                    Text(
                                        text = children.name,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}