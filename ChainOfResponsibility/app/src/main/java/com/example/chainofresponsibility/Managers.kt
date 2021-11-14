package com.example.chainofresponsibility

class AmericanoManager(override val beverageType: String) : CaffeManager() {
    override fun makeBeverage(menu: String): Boolean {
        //'beverageType' 문자열을 포함한 'menu' 요청 이라면 만들 수 있는 음료라고 가정
        return menu.contains(beverageType, true)
    }
}

class LatteManager(override val beverageType: String) : CaffeManager() {
    override fun makeBeverage(menu: String): Boolean {
        //'beverageType' 문자열을 포함한 'menu' 요청 이라면 만들 수 있는 음료라고 가정
        return menu.contains(beverageType, true)
    }
}

class AdeManager(override val beverageType: String) : CaffeManager() {
    override fun makeBeverage(menu: String): Boolean {
        //'beverageType' 문자열을 포함한 'menu' 요청 이라면 만들 수 있는 음료라고 가정
        return menu.contains(beverageType, true)
    }
}