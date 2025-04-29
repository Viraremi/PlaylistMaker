package com.practicum.playlistmaker.library.ui.fragments.playlists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.model.FragmentNewPlaylistState
import com.practicum.playlistmaker.library.ui.viewModel.playlists.FragmentNewPlaylistViewModel
import com.practicum.playlistmaker.util.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class FragmentNewPlaylist: Fragment() {

    companion object {
        const val FROM_PLAYER = "player"
    }

    var isFromPlayer = false

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FragmentNewPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var currentImage: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isFromPlayer = arguments?.getBoolean(FROM_PLAYER) ?: false

        viewModel.getState().observe(viewLifecycleOwner){ state ->
            when (state) {
                is FragmentNewPlaylistState.HAS_IMAGE -> {
                    binding.addPlaylistImage.setImageURI(state.uri)
                    currentImage = state.uri
                }
                is FragmentNewPlaylistState.EMPTY -> {
                    binding.addPlaylistImage.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.add_playlist_placeholder
                    )
                    currentImage = null
                }
            }
        }

        binding.addPlaylistBtnBack.setOnClickListener {
            closeFragmentWithAlert()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            closeFragmentWithAlert()
        }

        binding.addPlaylistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.addPlaylistBtnCreate.setOnClickListener {

            var image = ""
            val playlistname = binding.addPlaylistEdittextName.text.toString()
            if (currentImage != null) {
                saveImageToPrivateStorage(currentImage!!, playlistname + "_image")
                val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myplaylists")
                image = File(filePath, playlistname + ".jpg").toUri().toString()
            }
            viewModel.createOrUpdatePlaylist(
                Playlist(
                    0,
                    playlistname,
                    binding.addPlaylistEdittextDescription.text.toString(),
                    image,
                    listOf<Int>(),
                    0
                )
            )

            Toast.makeText(
                requireContext(),
                "Плейлист $playlistname создан",
                Toast.LENGTH_SHORT
            ).show()

            closeFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun closeFragment(){
        if (!isFromPlayer) (activity as RootActivity).animateBottomNavigationView()
        findNavController().popBackStack()
    }

    private fun closeFragmentWithAlert() {
        if (viewModel.getState().value is FragmentNewPlaylistState.HAS_IMAGE
            || binding.addPlaylistEdittextName.text.toString().isNotEmpty()
            || binding.addPlaylistEdittextDescription.text.toString().isNotEmpty()) {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNegativeButton("Отмена") { _, _ ->  /* none */ }
                .setPositiveButton("Завершить") { _, _ -> closeFragment() }
                .show()
        }
        else {
            closeFragment()
        }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setImage(uri)
            }
        }

    fun saveImageToPrivateStorage(uri: Uri, filename: String) {
        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "myplaylists"
            )
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath,filename + ".jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
}