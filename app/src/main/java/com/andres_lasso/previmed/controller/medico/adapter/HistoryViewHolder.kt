import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.controller.medico.medicoDAO.VisitHistoryDao
import com.andres_lasso.previmed.databinding.ItemVisitsHistoryBinding

class HistoryViewHolder(private val binding: ItemVisitsHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(visitHistory: VisitHistoryDao) {
        binding.txtName.text = visitHistory.name
        binding.txtLastName.text = visitHistory.lastName
        binding.txtSecondLastName.text = visitHistory.secondLastName
        binding.txtAddress.text = visitHistory.address
    }
}
