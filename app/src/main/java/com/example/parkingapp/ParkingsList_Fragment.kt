package com.example.parkingapp

import android.Manifest
import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.parkingapp.retrofit.Endpoint
import com.example.parkingapp.viewmodel.AdvancedSearchModel
import com.example.parkingapp.viewmodel.HoraireModel
import com.example.parkingapp.viewmodel.ParkingModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*



class ParkingsList_Fragment : Fragment(R.layout.fragment_parkings_list) {
    lateinit var parkingModel: ParkingModel
    lateinit var temp_parkingModel: ParkingModel
    private var loadingPB: ProgressBar? = null
    lateinit var adress: String
    lateinit var loc: Location
    var bool : Boolean = false
    lateinit var vm : HoraireModel
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var vm_Search:AdvancedSearchModel


    var advancedSearch = false

    lateinit var recyclerView: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(requireActivity()).get(HoraireModel::class.java)
        recyclerView = view?.findViewById(R.id.recyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        loadingPB = view.findViewById(R.id.loadingPB_ParkingList) as ProgressBar
        setHasOptionsMenu(true);






        Places.initialize(context,"AIzaSyCNdS-eHQeAsWyQ6xIEwROKmkgaA7zm6a4")

        vm_Search= ViewModelProvider(requireActivity()).get(AdvancedSearchModel::class.java)
        if(vm_Search.prix!="" && vm_Search.distance!=""){
            advancedSearch = true
            val fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(context)
            startActivityForResult(intent,100)
        }

            recyclerView.layoutManager = layoutManager
        parkingModel = ParkingModel()
        temp_parkingModel = ParkingModel()
        val vm_parking= ViewModelProvider(requireActivity()).get(ParkingModel::class.java)
        parkingModel.data = vm_parking.data


        recyclerView.adapter = ParkingAdapter(requireActivity(), parkingModel.data,vm)

        if(parkingModel.data.size == 0 ){
            statusCheck()
        }


        refreshFragment(view)

        var adapter = ParkingAdapter(context,parkingModel.data,vm)
        recyclerView.adapter = adapter

    }




    private fun refreshFragment(view:View) {
        swipeRefreshLayout = view.findViewById(R.id.swipe) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            statusCheck()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    fun loadData(){
        loadingPB!!.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            requireActivity().runOnUiThread() {
                loadingPB!!.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE

                Toast.makeText(requireActivity(), throwable.message, Toast.LENGTH_SHORT).show()
            }

        }
        loadingPB!!.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE

        getCurrentLocation()

        CoroutineScope(Dispatchers.IO+ exceptionHandler).launch {
            while (!bool){
                SystemClock.sleep(1000)
            }

            val response = Endpoint.createEndpoint().getAllParkings()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    parkingModel.data = response.body()!!.toMutableList()
                    temp_parkingModel.data.addAll(parkingModel.data)

                    for(parking in parkingModel.data){
                        parking.etat = "Ferm??"
                        calculateDistanceTime(parking)
                        for(horaire in parking.Horaire) {
                            if(horaire.jour== getCurrentDay()){
                                val sdf = SimpleDateFormat("HH:mm")
                                val currentDate = sdf.format(Date())
                                val time1 = currentDate.subSequence(0,2).toString().toInt() * 60 +currentDate.subSequence(3,5).toString().toInt()
                                val time2 = horaire.horaireOuverture.subSequence(11,13).toString().toInt() * 60 +horaire.horaireOuverture.subSequence(14,16).toString().toInt()
                                val time3 = horaire.horaireFermeture.subSequence(11,13).toString().toInt() * 60 +horaire.horaireFermeture.subSequence(14,16).toString().toInt()
                                if(time1>=time2 && time1<=time3){
                                    parking.etat = "Ouvert"
                                }
                            }
                        }
                    }
                    loadingPB!!.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.adapter = ParkingAdapter(requireActivity(), parkingModel.data,vm)
                } else {
                    Toast.makeText(requireActivity(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentDay():String{
        val calendar: Calendar = Calendar.getInstance()
        val day: Int = calendar.get(Calendar.DAY_OF_WEEK)
        var jour:String =""
        when (day) {
            Calendar.SUNDAY -> {jour="Dimanche"}
            Calendar.MONDAY -> {jour= "Lundi"}
            Calendar.TUESDAY -> {jour= "Mardi"}
            Calendar.WEDNESDAY -> {jour= "Mercredi"}
            Calendar.THURSDAY -> {jour= "Jeudi"}
            Calendar.FRIDAY -> {jour= "Vendredi"}
            Calendar.SATURDAY -> {jour= "Samedi"}
        }
        return jour
    }



    private fun getCurrentLocation(){

        val fusedLocationClient= LocationServices. getFusedLocationProviderClient(requireActivity())
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),2020)
        }else {
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        loc = location
                        adress = geocoder.getFromLocation(location.latitude,location.longitude,1)[0].getAddressLine(0)
                        bool = true

                    }
                }

        }
    }
    private fun calculateDistanceTime(parking: Parking){
        val loc_parking = Location("provider")
        loc_parking.latitude = parking.latitude.toDouble()
        loc_parking.longitude = parking.longitude.toDouble()


        val LatLng1 = LatLng(loc.latitude,loc.longitude)
        val LatLng2 = LatLng(loc_parking.latitude,loc_parking.longitude)
        val meter = SphericalUtil.computeDistanceBetween(LatLng1, LatLng2);
        //val meter: Float = loc.distanceTo(loc_parking)
        val kms = (meter / 1000).toDouble()

        var kms_per_min = 0.0

        if(kms<50){kms_per_min = 0.5}
        else if(kms>=50 && kms <100){kms_per_min = 1.67}
        else{kms_per_min = 2.0}

        val mins_taken = kms / kms_per_min

        val totalMinutes = mins_taken.toInt()


        val temp = if (totalMinutes < 60) {
            "$totalMinutes min"
        } else {
            var minutes = Integer.toString(totalMinutes % 60)
            minutes = if (minutes.length == 1) "0$minutes" else minutes
            (totalMinutes / 60).toString() + " heure " + minutes + " min"
        }
        parking.distance = kms.toString().subSequence(0,5).toString() + " Km - "
        parking.temps = temp
    }

    fun statusCheck() {

        val manager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }else{
            loadData()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Votre GPS semble ??tre d??sactiv??, voulez-vous l'activer ?")
            .setCancelable(false)
            .setPositiveButton("Oui",
                DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                    loadData()
                })
            .setNegativeButton("Non",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel()
                    Toast.makeText(requireActivity(), "GPS d??sactiv??, impossible de continuer", Toast.LENGTH_SHORT).show()
                })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        var item = menu.getItem(0)
        if (item != null) item.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.search_action){
            advancedSearch = false
            val fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(context)
            startActivityForResult(intent,100)
        }else if(item.itemId == R.id.btn_advancedSearch){
            val action = ParkingsList_FragmentDirections.actionParkingsListFragmentToDialogSearchFragment2()

            requireView().findNavController().navigate(action)
        }

        return super.onOptionsItemSelected(item)
    }


    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK){
            val place = Autocomplete.getPlaceFromIntent(data)

            temp_parkingModel.data.clear()

            val LatLng = place.latLng
            var LatLng_Park = LatLng(0.0,0.0)
            if (place.name.isNotEmpty()){
                parkingModel.data.forEach {
                    LatLng_Park = LatLng(it.latitude.toDouble(),it.longitude.toDouble())
                    if(advancedSearch == true){
                        if(SphericalUtil.computeDistanceBetween(LatLng, LatLng_Park)<=vm_Search.distance.toInt() * 1000 && it.tarifHeure<=vm_Search.prix.toInt()){
                            temp_parkingModel.data.add(it)
                        }
                        advancedSearch = false
                    }else{
                        if(SphericalUtil.computeDistanceBetween(LatLng, LatLng_Park)<10000){
                            temp_parkingModel.data.add(it)
                        }
                    }
                }
                recyclerView.adapter = ParkingAdapter(requireActivity(), temp_parkingModel.data,vm)

            }else{
                temp_parkingModel.data.clear()
                temp_parkingModel.data.addAll(parkingModel.data)
                recyclerView.adapter = ParkingAdapter(requireActivity(), temp_parkingModel.data,vm)
            }

        }else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            val status = Autocomplete.getStatusFromIntent(data)
            Toast.makeText(context,status.statusMessage, Toast.LENGTH_SHORT).show()

        }
        vm_Search.prix = ""
        vm_Search.distance = ""
    }

}