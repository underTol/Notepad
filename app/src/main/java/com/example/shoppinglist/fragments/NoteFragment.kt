package com.example.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.activities.MainApp
import com.example.shoppinglist.activities.NewNoteActivity
import com.example.shoppinglist.databinding.FragmentNoteBinding
import com.example.shoppinglist.db.MainViewModel
import com.example.shoppinglist.db.NoteAdapter
import com.example.shoppinglist.entities.NoteItem

class NoteFragment : BaseFragment(), NoteAdapter.Listener {
    private lateinit var binding: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NoteAdapter

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding){
        rcViewNote.layoutManager = LinearLayoutManager(activity)
        adapter = NoteAdapter(this@NoteFragment)
        rcViewNote.adapter= adapter
    }

    private fun observer(){
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun onEditResult(){
        editLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY, NoteItem::class.java))
                } else{
//                    Log.d("MyLog", "Build.VERSION.SDK_INT ${Build.VERSION.SDK_INT}")
//                    Log.d("MyLog", "Build.VERSION_CODES.TIRAMISU ${Build.VERSION_CODES.TIRAMISU}")
//                    @Suppress("DEPRECATION")
//                    val a = it.data?.getSerializableExtra(NEW_NOTE_KEY) as? NoteItem
//                    Log.d("MyLog", "it.data?.getSerializableExtra(NEW_NOTE_KEY) as? NoteItem \n $a")
                    @Suppress("DEPRECATION")
                    mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as? NoteItem)
                }
            }
        }
    }

    companion object {
        const val NEW_NOTE_KEY = "new_note_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }
    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote((id))
    }
}