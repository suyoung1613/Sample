package com.example.chainofresponsibility

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.chainofresponsibility.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOrder.setOnClickListener { processOrder(binding.etMenu.text.toString()) }
    }

    private fun processOrder(menu: String) {

        val firstWorker = AmericanoManager("Americano")

        firstWorker.setNextManager(LatteManager("Latte")).setNextManager(AdeManager("Lemonade"))

        val orderResult = firstWorker.processMenuOrder(menu)

        val result = "[메뉴 주문 결과] \n $orderResult"

        binding.tvOrderResult.text = result
        Log.i("COR", result)
    }
}