package ru.topbun.navigation.utills

import cafe.adriel.voyager.navigator.Navigator

tailrec fun Navigator.root(): Navigator = parent?.root() ?: this
