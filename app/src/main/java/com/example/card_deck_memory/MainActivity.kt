package com.example.card_deck_memory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var rvCards: RecyclerView
    private lateinit var tvIndex: TextView
    private lateinit var tvSeed: TextView
    private lateinit var btnPrev: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var cards: MutableList<Int>
    private var seed = -1

    private lateinit var adapter: CardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Memorize deck"

        setContentView(R.layout.activity_main)

        rvCards = findViewById(R.id.rvCards)

        tvIndex = findViewById(R.id.tvIndex)
        tvSeed = findViewById(R.id.tvSeed)

        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)

        setupDeck(seed)

        btnPrev.setOnClickListener {
            Log.i("MainActivity", "Clicked Prev")
            updateViewWithFlipCard(-1)
        }

        btnNext.setOnClickListener {
            Log.i("MainActivity", "Clicked Next")
            updateViewWithFlipCard(1)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_reset -> {
                setupDeck()
                return true
            }
            R.id.mi_seed -> {
                setNewSeed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setNewSeed() {
        var newSeedView = LayoutInflater.from(this).inflate(R.layout.seed_dialog, null)
        val pt_seed = newSeedView.findViewById<EditText>(R.id.pt_new_seed)

        showAlertDialog("", newSeedView, View.OnClickListener {
                seed = pt_seed.text.toString().toInt()
                if (seed > Int.MAX_VALUE) {
                    seed = seed % Int.MAX_VALUE
                }
                setupDeck(seed)
        })
    }

    private fun showAlertDialog(title: String, view : View?, positiveClickListener : View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
                positiveClickListener.onClick(null)
            }
            .show()
    }

    private fun setupDeck(seed: Int = -1) {
        if (seed == -1) {
            cards = DEFAULT_CARDS.toMutableList()
        } else {
            cards = DEFAULT_CARDS.shuffled(Random(seed)).toMutableList()
        }

        // Adding "no card" at the beginning of the list
        cards.add(0, R.drawable.no_card)

        adapter = CardsAdapter(this, 2, cards)

        rvCards.adapter = adapter
        rvCards.setHasFixedSize(true)
        rvCards.layoutManager = GridLayoutManager(this, 2)

        tvIndex.text = buildString {append(adapter.getIndex())
            append(" / 52")
        }

        tvSeed.text = buildString {append("Seed: ")
            append(seed)
        }
    }

    private fun updateViewWithFlipCard(deltaIndex : Int) {
        if (deltaIndex == 1) {
            adapter.increaseIndex()
        } else if (deltaIndex == -1) {
            adapter.decreaseIndex()
        } else {
            Log.w("MainActivity", "Something went wrong while changing index.")
        }
        tvIndex.text = buildString {append(adapter.getIndex())
            append(" / 52")
        }
        Log.i("MainActivity", "Updating after click: $deltaIndex")
        adapter.notifyDataSetChanged()
    }
}