import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.parkingapp.R
import com.example.parkingapp.data_class.Reservation
import com.example.parkingapp.retrofit.Endpoint
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class ReservationDialog : DialogFragment(R.layout.fragment_reservation_dialogue) {
    var startDate = Calendar.getInstance()
    var endTime = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startDateButton = view.findViewById(R.id.pick_start_date_button) as Button
        val selectedDate = view.findViewById(R.id.show_selected_date) as TextView
        val endDateButton = view.findViewById(R.id.pick_end_date_button) as Button
        val endDateText = view.findViewById(R.id.show_end_date) as TextView
        val verifyButton = view.findViewById<Button>(R.id.verify_reserver_button)


        // Set reservation selection
        val startDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                startDate.set(Calendar.YEAR, year)
                startDate.set(Calendar.MONTH, monthOfYear)
                startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(context, { view, hourOfDay, minute ->
                    startDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    startDate.set(Calendar.MINUTE, minute)
                }, startDate.get(Calendar.HOUR_OF_DAY), startDate.get(Calendar.MINUTE), false).show()

                updateDateInView(selectedDate, startDate)
            }
        }

        val endDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                endTime.set(Calendar.YEAR, year)
                endTime.set(Calendar.MONTH, monthOfYear)
                endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(context, { view, hourOfDay, minute ->
                    endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    endTime.set(Calendar.MINUTE, minute)
                }, endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE), false).show()

                updateDateInView(endDateText, endTime)
            }
        }

        startDateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(requireActivity(),
                    startDateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    startDate.get(Calendar.YEAR),
                    startDate.get(Calendar.MONTH),
                    startDate.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        endDateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                TimePickerDialog(context, { view, hourOfDay, minute ->
                    endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    endTime.set(Calendar.MINUTE, minute)
                }, endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE), false).show()

                updateDateInView(endDateText, endTime)
            }
        })

        // Verify button listener
        verifyButton.setOnClickListener {
            /** Verifier si il est possible de r??server la place **/

            println("Verify " + startDate.time.toString() + " " + endTime.time.toString())
            // Obtenir l'id du parking et du compte
            val idParking = arguments?.getInt("idParking")
            val idCompte = arguments?.getInt("idCompte")

            // Inserting Coroutine
            val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
                val text = "Probl??me de connextion"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
                throwable.printStackTrace()
            }
            CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
                println("Send request")
                val response = Endpoint.createEndpoint().getReservationsByParking(idParking.toString())
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        println("request seccesful")

                        var data = response.body()!!
                        val chosenDateReservations :List<Reservation> =  data.filter { reservation ->
                            reservation.dateReservation.day == startDate.get(Calendar.DAY_OF_MONTH) &&
                                reservation.dateReservation.month == startDate.get(Calendar.MONTH) &&
                                reservation.dateReservation.year == startDate.get(Calendar.YEAR)
                        }

                        /*****************************************/
                        // Iterer sur les r??servations du parking du jour choisi
                        // pour v??rifier s'il est vide durant le temps choisi
                        val timeFormat = "yyyy MM dd hh:mm"
                        var sdf: SimpleDateFormat = SimpleDateFormat(timeFormat)
                        var dateDebutReservation = Date()
                        var dateFinReservation = Date()
                        var faireReservation: Boolean =true
                        // For each
                        chosenDateReservations.forEach { reservation ->
                            var formedDateString =
                                startDate.get(Calendar.YEAR).toString() + " "+startDate.get(Calendar.MONTH).toString() + " "+startDate.get(Calendar.DAY_OF_MONTH).toString() + " " + reservation.heureEntre
                            dateDebutReservation = sdf.parse(formedDateString)
                            formedDateString =
                                startDate.get(Calendar.YEAR).toString() + " "+startDate.get(Calendar.MONTH).toString() + " "+startDate.get(Calendar.DAY_OF_MONTH).toString() + " " + reservation.heureSortie
                            dateFinReservation = sdf.parse(formedDateString)
                            // V??rifier si le temps choisi entrelace avec le temps d'une r??servation
                            // I.e entre le d??but et le fin de la r??servation
                            if ((startDate.time.after(dateDebutReservation) && startDate.time.before(dateFinReservation) ||
                                        (endTime.after(dateDebutReservation) && endTime.before(dateFinReservation))) )
                            {
                                faireReservation = false
                            }
                        }

                        if (faireReservation) {
                            var heureEntre: String =
                                startDate.get(Calendar.HOUR_OF_DAY).toString() + ":" + startDate.get(Calendar.MINUTE)
                                    .toString()
                            var heureSortie: String =
                                endTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + endTime.get(Calendar.MINUTE)
                                    .toString()

                            val PLACE_PARKING = "10"
                            var newReservation: Reservation = Reservation(
                                0,
                                startDate.time,
                                heureEntre,
                                heureSortie,
                                PLACE_PARKING,
                                true,
                                idParking,
                                idCompte
                            )
                            val response = Endpoint.createEndpoint().createReservation(newReservation)
                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful && response.body() != null) {
                                    val text = "Place r??serv??, numero de reservation est " + response.body()!!.numReservation
                                    val duration = Toast.LENGTH_SHORT
                                    val toast = Toast.makeText(context, text, duration)
                                    toast.show()
                                }
                            }
                        } else {
                            val text = "Toutes les places R??serv??e"
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, text, duration)
                            toast.show()
                        }
                        /********************************/
                    }
                }
            }
        }
    }


    private fun updateDateInView(selectedDate: TextView, date: Calendar) {
        val myFormat = "MM/dd/yyyy '??' hh:mm" 
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        selectedDate!!.text = sdf.format(date.getTime())
    }

    fun getQrCodeBitmap(s: String): Bitmap {
        val size = 512 //pixels
        val qrCodeContent = s
        val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 } // Make the QR code buffer border narrower
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size)
        return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}
