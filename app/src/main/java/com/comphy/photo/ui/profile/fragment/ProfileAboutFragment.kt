package com.comphy.photo.ui.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.data.source.remote.response.user.detail.Experience
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.BottomSheetAddExperienceBinding
import com.comphy.photo.databinding.FragmentProfileAboutBinding
import com.comphy.photo.ui.profile.ExperienceAdapter
import com.comphy.photo.ui.profile.ProfileActivity
import com.comphy.photo.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch

class ProfileAboutFragment : Fragment() {

    private var _binding: FragmentProfileAboutBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetAddExperienceBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetAddExperienceBinding.inflate(layoutInflater)
    }
    private val viewModel: ProfileViewModel by activityViewModels()
    private var isEditExperience = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.userData.observe(viewLifecycleOwner) { setupWidgets(it) }
    }

    private fun setupWidgets(userData: UserResponseData) {
        with(binding) {
            val extraId = (activity as ProfileActivity).extraId
            val userId = (activity as ProfileActivity).userAuth.userId

            if (extraId == userId) {
                btnAddExperience.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        isEditExperience = false
                        showBottomSheetAddExperience()
                    }
                }
            } else btnAddExperience.visibility = View.INVISIBLE

            txtAboutContent.text = userData.description

            if (userData.experiences?.isEmpty()!!) {
                if (extraId == userId) {
                    txtExperienceEmpty.visibility = View.VISIBLE
                    txtOtherExperienceEmpty.visibility = View.GONE
                } else {
                    txtExperienceEmpty.visibility = View.GONE
                    txtOtherExperienceEmpty.visibility = View.VISIBLE
                }
                rvExperience.visibility = View.GONE

            } else {
                txtExperienceEmpty.visibility = View.GONE
                txtOtherExperienceEmpty.visibility = View.GONE
                rvExperience.apply {
                    visibility = View.VISIBLE
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = ExperienceAdapter(userData.experiences!!) { experience, pos ->
                        isEditExperience = true
                        showBottomSheetAddExperience(experience, pos)
                    }
                }
            }

            txtSocmedContent.text = userData.socialMedia
        }
    }

    private fun showBottomSheetAddExperience(
        experience: Experience? = null,
        position: Int? = null
    ) {
        with(bottomSheetAddExperienceBinding) {
            val inputWidgets =
                listOf(edtExperiencePosition, edtExperienceInstance, edtExperienceYear)
            val editExperienceField =
                listOf(experience?.position, experience?.company, experience?.years)

            fun isFieldNotNecessary(): Boolean {
                inputWidgets.forEach { edt ->
                    if (isEditExperience) {
                        if (edt.text.isEmpty()
                            || edt.text == editExperienceField[inputWidgets.indexOf(edt)]
                        ) return true
                    } else {
                        if (edt.text.isEmpty()) return true
                    }
                }
                return false
            }

            if (isEditExperience) {
                btnDeleteExperience.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        lifecycleScope.launch {
                            viewModel.deleteExperience(experience?.id!!)
                            (activity as ProfileActivity).bottomSheetDialog.dismiss()
                        }
                    }
                }
                edtExperienceInstance.text = experience!!.company
                edtExperiencePosition.text = experience.position
                edtExperienceYear.text = experience.years
                inputWidgets.forEach { edt ->
                    edt.doAfterTextChanged { btnSave.isEnabled = !isFieldNotNecessary() }
                }
                btnSave.setOnClickListener {
                    lifecycleScope.launch {
                        viewModel.updateExperience(
                            experience.id,
                            edtExperienceInstance.text.toString(),
                            edtExperiencePosition.text.toString(),
                            edtExperienceYear.text.toString(),
                            position!!
                        )
                        inputWidgets.forEach { it.text = "" }
                        (activity as ProfileActivity).bottomSheetDialog.dismiss()
                    }
                }
            } else {
                btnDeleteExperience.visibility = View.GONE
                inputWidgets.forEach { edt ->
                    edt.doAfterTextChanged { btnSave.isEnabled = !isFieldNotNecessary() }
                }
                btnSave.setOnClickListener {
                    lifecycleScope.launch {
                        viewModel.createExperience(
                            edtExperienceInstance.text.toString(),
                            edtExperiencePosition.text.toString(),
                            edtExperienceYear.text.toString()
                        )
                        inputWidgets.forEach { it.text = "" }
                        (activity as ProfileActivity).bottomSheetDialog.dismiss()
                    }
                }
            }
        }

        (activity as ProfileActivity).bottomSheetDialog.setContentView(bottomSheetAddExperienceBinding.root)
        (activity as ProfileActivity).bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        (activity as ProfileActivity).viewPagerSetupHelper.setProperHeightOfView(binding.root)
    }
}