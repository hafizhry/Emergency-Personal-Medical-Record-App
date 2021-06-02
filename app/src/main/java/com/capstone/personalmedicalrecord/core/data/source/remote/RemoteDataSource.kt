package com.capstone.personalmedicalrecord.core.data.source.remote

import android.util.Log
import com.capstone.personalmedicalrecord.core.data.source.remote.network.ApiResponse
import com.capstone.personalmedicalrecord.core.data.source.remote.response.NoteResponse
import com.capstone.personalmedicalrecord.core.data.source.remote.response.PatientResponse
import com.capstone.personalmedicalrecord.core.data.source.remote.response.RecordResponse
import com.capstone.personalmedicalrecord.core.data.source.remote.response.StaffResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class RemoteDataSource {
    private val db = FirebaseFirestore.getInstance()

    private val noteDb = db.collection("note")
    private val patientDb = db.collection("patient")
    private val recordDb = db.collection("record")
    private val staffDb = db.collection("staff")

    fun getNotes(idPatient: String): Flow<ApiResponse<List<NoteResponse>>> {
        return flow {
            val data = noteDb.get().await()
            val notes = data.toObjects(NoteResponse::class.java)
            if (notes.isNotEmpty()) {
                val list = notes.filter { it.idPatient == idPatient }
                Log.d("getNotes", list.toString())
                emit(ApiResponse.Success(list))
            } else {
                emit(ApiResponse.Success(arrayListOf<NoteResponse>()))
            }
        }
    }

    fun getNoteDetail(id: String): Flow<ApiResponse<NoteResponse>> {
        return flow {
            val data = noteDb.document(id).get().await()
            val note = data.toObject(NoteResponse::class.java) as NoteResponse
            Log.d("getNoteDetail", note.toString())
            emit(ApiResponse.Success(note))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertNote(note: NoteResponse): String {
        return try {
            val id = noteDb.document().id
            note.id = id
            noteDb.document(id).set(note)
                .addOnSuccessListener {
                    Log.d("insertNote", "Saved to DB")
                }
                .addOnFailureListener {
                    Log.d("insertNote", "Error saving to DB")
                }
                .await()
            id
        } catch (e: Exception) {
            Log.e("insertNote", e.message.toString())
            ""
        }
    }

    suspend fun insertPatient(patient: PatientResponse): String {
        return try {
            val id = patientDb.document().id
            patient.id = id
            patientDb.document(id).set(patient)
                .addOnSuccessListener {
                    Log.d("insertPatient", "Saved to DB")
                }
                .addOnFailureListener {
                    Log.e("insertPatient", "Error saving to DB")
                }
                .await()
            id
        } catch (e: Exception) {
            Log.e("insertPatient", e.message.toString())
            ""
        }
    }

    fun updatePatient(patient: PatientResponse) {
        try {
            patientDb.document(patient.id).set(patient)
                .addOnSuccessListener {
                    Log.d("updatePatient", "Update DB")
                }
                .addOnFailureListener {
                    Log.e("updatePatient", "Error update in DB")
                }
        } catch (e: Exception) {
            Log.e("updatePatient", e.message.toString())
        }
    }

    fun updatePicturePatient(id: String, uri: String) {
        try {
            patientDb.document(id).update(mapOf("picture" to uri))
                .addOnSuccessListener {
                    Log.d("updatePicturePatient", "Success to Change Picture")
                }
                .addOnFailureListener {
                    Log.d("updatePicturePatient", "Error saving to DB")
                }
        } catch (e: Exception) {
            Log.e("updatePicturePatient", e.message.toString())
        }
    }

    fun getPatientDetail(id: String): Flow<ApiResponse<PatientResponse>> {
        return flow {
            val data = patientDb.document(id).get().await()
            val patient = data.toObject(PatientResponse::class.java) as PatientResponse
            Log.d("getPatientDetail", patient.toString())
            emit(ApiResponse.Success(patient))
        }.flowOn(Dispatchers.IO)
    }

    fun getPatient(email: String): Flow<ApiResponse<PatientResponse>> {
        return flow {
            val data = patientDb.get().await()
            val patients = data.toObjects(PatientResponse::class.java)
            if (patients.isNotEmpty()) {
                var isAvailable = true
                for (patient in patients) {
                    if (patient.email == email) {
                        isAvailable = false
                        Log.d("getPatient", patient.toString())
                        emit(ApiResponse.Success(patient))
                        break
                    }
                }
                if (isAvailable) {
                    emit(ApiResponse.Success(PatientResponse()))
                }
            } else {
                emit(ApiResponse.Success(PatientResponse()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getRecords(idPatient: String): Flow<ApiResponse<List<RecordResponse>>> {
        return flow {
            val data = recordDb.get().await()
            val records = data.toObjects(RecordResponse::class.java)
            if (records.isNotEmpty()) {
                val list = records.filter { it.idPatient == idPatient }
                Log.d("getRecords", list.toString())
                emit(ApiResponse.Success(list))
            } else {
                emit(ApiResponse.Success(arrayListOf<RecordResponse>()))
            }
        }
    }

    fun getRecordDetail(id: String): Flow<ApiResponse<RecordResponse>> {
        return flow {
            val data = recordDb.document(id).get().await()
            val record = data.toObject(RecordResponse::class.java) as RecordResponse
            Log.d("getRecordDetail", record.toString())
            emit(ApiResponse.Success(record))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertRecord(record: RecordResponse): String {
        return try {
            val id = recordDb.document().id
            record.id = id
            recordDb.document(id).set(record)
                .addOnSuccessListener {
                    Log.d("insertRecord", "Saved to DB")
                }
                .addOnFailureListener {
                    Log.d("insertRecord", "Error saving to DB")
                }
                .await()
            id
        } catch (e: Exception) {
            Log.e("insertRecord", e.message.toString())
            ""
        }
    }

    fun updateStaff(staff: StaffResponse) {
        try {
            staffDb.document(staff.id).set(staff)
                .addOnSuccessListener {
                    Log.d("updateStaff", "Update DB")
                }
                .addOnFailureListener {
                    Log.e("updateStaff", "Error update in DB")
                }
        } catch (e: Exception) {
            Log.e("updateStaff", e.message.toString())
        }
    }

    fun getStaffDetail(id: String): Flow<ApiResponse<StaffResponse>> {
        return flow {
            val data = staffDb.document(id).get().await()
            val staff = data.toObject(StaffResponse::class.java) as StaffResponse
            Log.d("getStaffDetail", staff.toString())
            emit(ApiResponse.Success(staff))
        }.flowOn(Dispatchers.IO)
    }

    fun getStaff(email: String): Flow<ApiResponse<StaffResponse>> {
        return flow {
            val data = staffDb.get().await()
            val staffs = data.toObjects(StaffResponse::class.java)
            if (staffs.isNotEmpty()) {
                var isAvailable = true
                for (staff in staffs) {
                    if (staff.email == email) {
                        isAvailable = false
                        Log.d("getStaff", staff.toString())
                        emit(ApiResponse.Success(staff))
                        break
                    }
                }
                if (isAvailable) {
                    emit(ApiResponse.Success(StaffResponse()))
                }
            } else {
                emit(ApiResponse.Success(StaffResponse()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertStaff(staff: StaffResponse): String {
        return try {
            val id = staffDb.document().id
            staff.id = id
            staffDb.document().set(staff)
                .addOnSuccessListener {
                    Log.d("insertStaff", "Saved to DB")
                }
                .addOnFailureListener {
                    Log.e("insertStaff", "Error saving to DB")
                }
                .await()
            id
        } catch (e: Exception) {
            Log.e("insertStaff", e.message.toString())
            ""
        }
    }
}