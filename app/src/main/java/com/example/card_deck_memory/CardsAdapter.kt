package com.example.card_deck_memory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class CardsAdapter(private val context: Context,
                   private val numCards: Int,
                   private val shuffledCards: MutableList<Int>,
                   private var index : Int = 0) :
    RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    companion object {
        private const val MARGIN_SIZE = 8
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardWidth = min(parent.width / 2, (parent.height * (57.0 / 89.0)).toInt()) - 2 * MARGIN_SIZE
        val cardHeight = (cardWidth * 89.0 / 57.0).toInt() - 2 * MARGIN_SIZE

        val view = LayoutInflater.from(context).inflate(R.layout.deck_card, parent, false)
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as MarginLayoutParams
        layoutParams.width = cardWidth
        layoutParams.height = cardHeight
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)

        return ViewHolder(view)
    }

    override fun getItemCount() = numCards

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageCards = itemView.findViewById<ImageButton>(R.id.btnDeck)
        fun bind(position: Int) {

            if (position == 0) {
                if (index == 52) {
                    imageCards.setImageResource(R.drawable.no_card)
                } else {
                    imageCards.setImageResource(R.drawable.card_back)
                }
            }

            // Only showing cards on the right space
            if (position == 1) {
                Log.i("CardsAdapter", "Binging/rebinding. Index: $index")
                imageCards.setImageResource(shuffledCards[index])
            }
        }
    }

    fun increaseIndex() {
        if (index < 52) {
            index = index + 1
        }
    }

    fun decreaseIndex() {
        if (index > 0) {
            index = index - 1
        }
    }

    fun getIndex(): Int = index
}
