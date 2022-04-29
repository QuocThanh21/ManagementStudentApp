package com.example.testnavigation

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.GridLayout.HORIZONTAL
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.RealmConfiguration


class StudentManagementFragment : Fragment() {

    lateinit var realmDb: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_management, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creating our db with custom properties
        val config = RealmConfiguration.Builder()
            .name("realmStudent.realm")
            .schemaVersion(1)
            .build()
        Realm.setDefaultConfiguration(config)

        // Get a Realm instance for this thread
        realmDb = Realm.getDefaultInstance()

        //try {
            /*val jsonString = getJSONFromAssets()!!
            val studentsList = Gson().fromJson(jsonString, Students::class.java)*/

        val studentsList: MutableList<Student> = realmDb.where(Student::class.java).findAll()
        val rvStudentsList = view.findViewById<RecyclerView>(R.id.rvStudentsList)

        // Set the LayoutManager that this RecyclerView will use.
        rvStudentsList.layoutManager = GridLayoutManager(view.context, 2)
        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = StudentAdapter(view.context, studentsList) { std: Student ->
                run {
                    //Display Edit-Del student Dialog
                    val alInfo = AlertDialog.Builder(view.context)
                    val view: View = layoutInflater.inflate(R.layout.info_student_dialog, null)
                    alInfo.setView(view)
                    val alertInfoDialog = alInfo.show()

                    //Get objects in dialog
                    val etIdInfo = view.findViewById<EditText>(R.id.etIdInfo)
                    val etNameInfo = view.findViewById<EditText>(R.id.etNameInfo)
                    val ivImageInfo = view.findViewById<ImageView>(R.id.ivImageInfo)
                    val etImageInfo = view.findViewById<EditText>(R.id.etImageInfo)
                    val btnDelete = view.findViewById<Button>(R.id.btnDelete)
                    val btnEdit = view.findViewById<Button>(R.id.btnEdit)
                    val btnCloseInfo = view.findViewById<FloatingActionButton>(R.id.btnCloseInfo)
                    val position = studentsList.indexOf(std)

                    //Set values for fields of dialog
                    etIdInfo.setText(std.id.toString())
                    etNameInfo.setText(std.name)
                    Glide.with(alertInfoDialog.context)
                        .load(std.image)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.no_image_found)
                        .fitCenter()
                        .into(ivImageInfo)
                    etImageInfo.setText(std.image)

                    //Set tag button Edit, Delete
                    btnEdit.tag = "Edit"
                    btnDelete.tag  = "Delete"

                    //Delete student
                    btnDelete.setOnClickListener {
                        //Delete
                        if (btnDelete.tag == "Delete") {
                            alertInfoDialog.dismiss()

                            //Remove student in realmDB
                            realmDb.executeTransaction {
                                std.deleteFromRealm()
                            }
                            // Reload current fragment
                            findNavController().navigate(R.id.studentManagementFragment)
                            //Move view to the deleted student
                            rvStudentsList.smoothScrollToPosition(position)
                            Toast.makeText(btnDelete.context, "Deleted student successfully", Toast.LENGTH_LONG).show()
                        }

                        //If click Cancel button when Edit student
                        if (btnDelete.tag == "Cancel") {
                            //Rename button back to original
                            btnEdit.text = "Edit"
                            btnEdit.backgroundTintList = ColorStateList.valueOf(R.color.green)
                            btnDelete.text = "Delete"

                            //Set values for fields of dialog back to original
                            etNameInfo.setText(std.name)
                            Glide.with(alertInfoDialog.context)
                                .load(std.image)
                                .placeholder(R.drawable.placeholder_image)
                                .error(R.drawable.no_image_found)
                                .fitCenter()
                                .into(ivImageInfo)
                            etImageInfo.setText(std.image)

                            //Set UI EditText back to original
                            etNameInfo.backgroundTintList = ColorStateList.valueOf(R.color.white)
                            etNameInfo.isEnabled = false
                            etImageInfo.isEnabled = false
                            etImageInfo.backgroundTintList = ColorStateList.valueOf(R.color.white)

                            //Change tag button Edit, Delete back to original
                            btnEdit.tag = "Edit"
                            btnDelete.tag = "Delete"
                        }

                    }

                    //Edit student
                    btnEdit.setOnClickListener {
                        //Edit
                        if (btnEdit.tag == "Edit") {
                            //Enabled fields
                            etNameInfo.isEnabled = true
                            etNameInfo.backgroundTintList = ColorStateList.valueOf(R.color.gray)

                            println(etNameInfo.backgroundTintList)

                            etImageInfo.isEnabled = true
                            etImageInfo.backgroundTintList = ColorStateList.valueOf(R.color.gray)


                            //Rename button to edit
                            btnEdit.text = "Save"
                            btnEdit.backgroundTintList = ColorStateList.valueOf(R.color.blue)
                            btnDelete.text = "Cancel"

                            //Change tag button Edit, Delete
                            btnEdit.tag = "Save"
                            btnDelete.tag = "Cancel"

                        } else {
                            //If click Save button
                            if (btnEdit.tag == "Save") {
                                //Get value in fields
                                val nameEdit = etNameInfo.text.toString()
                                val imageEdit = etImageInfo.text.toString()

                                //Check fill fields
                                if (nameEdit == "" || imageEdit == "") {
                                    Toast.makeText(
                                        alertInfoDialog.context,
                                        "Please fill out all fields!!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {

                                    //Get edit student
                                    var studentEdit =
                                        realmDb.where(Student::class.java).equalTo("id", std.id)
                                            .findFirst()

                                    //Edit student in RealmDB
                                    realmDb.executeTransaction {
                                        if (studentEdit != null) {
                                            studentEdit.name = nameEdit
                                            studentEdit.image = imageEdit
                                        }
                                    }

                                    //Set edit values for fields of dialog
                                    etNameInfo.setText(nameEdit)
                                    Glide.with(alertInfoDialog.context)
                                        .load(std.image)
                                        .placeholder(R.drawable.placeholder_image)
                                        .error(R.drawable.no_image_found)
                                        .fitCenter()
                                        .into(ivImageInfo)
                                    etImageInfo.setText(imageEdit)

                                    //Rename button back to original
                                    btnEdit.text = "Edit"
                                    btnEdit.backgroundTintList =
                                        ColorStateList.valueOf(R.color.green)
                                    btnDelete.text = "Delete"

                                    //Change tag button Edit, Delete back to original
                                    btnEdit.tag = "Edit"
                                    btnDelete.tag = "Delete"

                                    //Set UI EditText back to original
                                    etNameInfo.backgroundTintList =
                                        ColorStateList.valueOf(R.color.red)
                                    etNameInfo.isEnabled = false
                                    etImageInfo.backgroundTintList =
                                        ColorStateList.valueOf(R.color.red)

                                    println(etNameInfo.backgroundTintList)

                                    etImageInfo.isEnabled = false

                                    //Notify
                                    Toast.makeText(
                                        context,
                                        "Edited student successfully!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }

                    //Close dialog when clicking close button
                    btnCloseInfo.setOnClickListener{
                        alertInfoDialog.dismiss()
                        // Reload current fragment
                        findNavController().navigate(R.id.studentManagementFragment)
                        //Move view to the deleted student
                        //rvStudentsList.smoothScrollToPosition(position)
                    }
                }
            }

        itemAdapter.notifyDataSetChanged()

        // adapter instance is set to the recyclerview to inflate the items.
        rvStudentsList.adapter = itemAdapter

        //Add new student
        view.findViewById<FloatingActionButton>(R.id.btnAddStd).setOnClickListener {
            //Display Add student Dialog
            val al = AlertDialog.Builder(view.context)
            val view: View = layoutInflater.inflate(R.layout.add_student_dialog, null)
            al.setView(view)
            val alertDialog = al.show()

            //Get objects in dialog
            val etId = view.findViewById<EditText>(R.id.etId)
            val etName = view.findViewById<EditText>(R.id.etName)
            val etImage = view.findViewById<EditText>(R.id.etImage)
            val btnAddDialog = view.findViewById<Button>(R.id.btnAddDialog)
            val btnCloseAdd = view.findViewById<FloatingActionButton>(R.id.btnCloseAdd)

            btnAddDialog.setOnClickListener {

                val idAdd = etId.text.toString()
                val nameAdd = etName.text.toString()
                val imageAdd = etImage.text.toString()

                if (idAdd == "" || nameAdd == "" || imageAdd == "") {
                    Toast.makeText(btnAddDialog.context, "Please fill out all fields!!", Toast.LENGTH_LONG).show()
                } else {
                    //Check id
                    var existId = realmDb.where(Student::class.java).equalTo("id", idAdd.toInt()).findFirst()?.id
                    if (existId != null) {
                        Toast.makeText(btnAddDialog.context, "ERROR!! ID already exists!", Toast.LENGTH_LONG).show()
                    } else {
                        alertDialog.dismiss()

                        //Create new student
                        var newStudent = Student().apply {
                            name = nameAdd
                            id = idAdd.toInt()
                            image = imageAdd
                        }
                        //Add student to RealmDB
                        realmDb.executeTransaction {
                            it.insert(newStudent)
                        }

                        //Notify to reload the new item
                        itemAdapter.notifyItemInserted(itemAdapter.items.size - 1)
                        //Move view to the new student
                        rvStudentsList.smoothScrollToPosition(itemAdapter.items.size - 1)
                        Toast.makeText(context, "Added new student successfully!", Toast.LENGTH_LONG).show()
                    }
                }
            }

            //Close dialog when clicking close button
            btnCloseAdd.setOnClickListener{
                alertDialog.dismiss()
            }
        }

/*        } catch (e: JSONException) {
            //exception
            e.printStackTrace()
            println("Error when converting json to class")
          } */

    }


    /**
     * Method to load the JSON from the Assets file and return the object
     */
   /* private fun getJSONFromAssets(): String? {

        var json: String? = null
        val charset: Charset = Charsets.UTF_8
        try {
            val myStudentsJSONFile = requireContext().assets.open("student.json")
            val size = myStudentsJSONFile.available()
            val buffer = ByteArray(size)
            myStudentsJSONFile.read(buffer)
            myStudentsJSONFile.close()
            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            println("Error when getting Json string")
            return null
        }
        return json
    }
*/
}