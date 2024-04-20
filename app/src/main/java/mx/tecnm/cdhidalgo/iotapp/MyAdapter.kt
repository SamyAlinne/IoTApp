package mx.tecnm.cdhidalgo.iotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val dataset: Array<Array<String?>>?, private val listener: ItemListener): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        class ViewHolder(vitemSensor : View, listener: ItemListener): RecyclerView.ViewHolder(vitemSensor) {
            var tvItemId: TextView
            var tvItemType: TextView
            var tvItemValue: TextView
            var tvItemName: TextView
            var tvItemDate: TextView
            var btnItemEdit: Button
            var btnItemDelete: Button
            init {
                tvItemId = vitemSensor.findViewById(R.id.txtItemId)
                tvItemType = vitemSensor.findViewById(R.id.txtItemTipo)
                tvItemValue = vitemSensor.findViewById(R.id.txtItemValor)
                tvItemName = vitemSensor.findViewById(R.id.txtItemNombre)
                tvItemDate = vitemSensor.findViewById(R.id.txtItemFecha)
                btnItemEdit = vitemSensor.findViewById(R.id.btnItemEdit)
                btnItemDelete = vitemSensor.findViewById(R.id.btnItemDelete)

                btnItemEdit.setOnClickListener { view -> listener.onEdit(view, adapterPosition) }
                btnItemDelete.setOnClickListener { view -> listener.onDel(view, adapterPosition) }
                vitemSensor.setOnClickListener { view -> listener.onClick(view, adapterPosition) }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val vitemSensor = LayoutInflater.from(parent.context).inflate(R.layout.item_sensor, parent, false)
        return ViewHolder(vitemSensor, listener)
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        holder.tvItemId.text = dataset!![position][0]
        holder.tvItemName.text = dataset!![position][1]
        holder.tvItemType.text = dataset!![position][2]
        holder.tvItemValue.text = dataset!![position][3]
        holder.tvItemDate.text = dataset!![position][4]
    }

    override fun getItemCount(): Int {
        return dataset!!.size
    }
}
