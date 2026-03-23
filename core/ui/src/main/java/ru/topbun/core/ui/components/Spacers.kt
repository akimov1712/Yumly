package ru.topbun.core.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp

@Composable
fun Height(height: Dp) = Spacer(Modifier.height(height))

@Composable
fun Width(width: Dp) = Spacer(Modifier.width(width))

@Composable
fun ColumnScope.Weight(weight: Float) = Spacer(Modifier.weight(weight))

@Composable
fun RowScope.Weight(weight: Float) = Spacer(Modifier.weight(weight))