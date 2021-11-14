package com.example.chainofresponsibility

abstract class CaffeManager {
    //카페 주문 처리 프로세스
    fun processMenuOrder(menu: String): String {
        return if (makeBeverage(menu)) {
            "Success To Make [$beverageType]"
        } else {
            nextManager?.processMenuOrder(menu)
                ?: "Fail To Make Beverage : No Manager To Make $menu"
        }
    }

    //만들 수 있는 음료 종류
    abstract val beverageType: String

    //음료 만들기
    abstract fun makeBeverage(menu: String): Boolean //추상 메소드

    //내가 처리 못하는 주문일 경우 담당할 NextManager 지정
    private var nextManager: CaffeManager? = null
    fun setNextManager(nextManager: CaffeManager): CaffeManager {
        this.nextManager = nextManager
        return nextManager
    }


}


