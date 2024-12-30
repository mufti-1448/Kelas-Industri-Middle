package com.mufti.kl_midel

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mufti.kl_midel.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MvvmViewModel
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Mengatur View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur padding untuk inset sistem
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this, MvvmViewModel.Factory)[MvvmViewModel::class.java]

        // Mengamati perubahan data ViewModel
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.counter.collect {
                    counter = it
                    binding.tvCount.text = it.toString()
                }
            }
        }

        // Menampilkan nilai awal counter
        binding.tvCount.text = counter.toString()

        // Tombol (-) untuk mengurangi nilai counter
        binding.btnMin.setOnClickListener {
            lifecycleScope.launch {
                viewModel.decrease(counter)
            }
        }

        // Tombol (+) untuk menambah nilai counter
        binding.btnPlus.setOnClickListener {
            lifecycleScope.launch {
                viewModel.increase(counter)
            }
        }
    }
}
