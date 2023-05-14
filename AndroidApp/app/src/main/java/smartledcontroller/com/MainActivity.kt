package smartledcontroller.com

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.skydoves.colorpickerview.listeners.ColorPickerViewListener
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSpinnersValues()
        initCameraInfoForUsingTorch()
    }

    private fun initSpinnersValues()
    {
        initChipsetSpinner()
        initAnimationModeSpinner()
    }

    private fun initChipsetSpinner()
    {
        spinnerChipsets=findViewById(R.id.spinner_chipsets)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.led_strip_chipsets))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerChipsets.adapter = adapter
    }

    private fun initAnimationModeSpinner()
    {
        spinnerAnimModes=findViewById(R.id.spinner_animationModes)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.led_animation_modes))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAnimModes.adapter = adapter
    }

    private lateinit var spinnerChipsets: Spinner
    private lateinit var spinnerAnimModes: Spinner

    private lateinit var cameraId: String
    private lateinit var cameraManager: CameraManager
    private var torchState by Delegates.notNull<Boolean>()

    private fun initCameraInfoForUsingTorch()
    {
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        // Find the rear-facing camera
        for (id in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                cameraId = id
                break
            }
        }

        torchState = true
        updateTorchState()
    }

    private fun updateTorchState() {
        torchState = !torchState
        cameraManager.setTorchMode(cameraId, torchState)
    }

    fun updateTorchState(view: View) {
        updateTorchState()
    }

    fun onClickColorPicker(view: View) {
        val builder = ColorPickerDialog.Builder(this)
        builder.setTitle("Please select a color")
        builder.setPreferenceName("Please select a color")
        builder.setPositiveButton("Confirm", object : ColorEnvelopeListener {
            override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                view.setBackgroundColor(envelope!!.color)
            }
        })
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        builder.show()
    }
}