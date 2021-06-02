package com.capstone.personalmedicalrecord.core.domain.repository

import com.capstone.personalmedicalrecord.core.data.Resource
import com.capstone.personalmedicalrecord.core.domain.model.Note
import com.capstone.personalmedicalrecord.core.domain.model.Patient
import com.capstone.personalmedicalrecord.core.domain.model.Record
import com.capstone.personalmedicalrecord.core.domain.model.Staff
import kotlinx.coroutines.flow.Flow

interface IRepository {
    fun getNotes(idPatient: String): Flow<List<Note>>
    fun getNote(id: String): Flow<Note>
    fun insertNote(note: Note)
    fun updateNote(note: Note)
    fun deleteNote(id: String)

    fun getPatients(): Flow<List<Patient>>
    fun getPatientDetail(id: String): Flow<Resource<Patient>>
    fun getPatient(email: String): Flow<Resource<Patient>>
    suspend fun insertPatient(patient: Patient): String
    fun updatePatient(patient: Patient)
    fun updatePicturePatient(id: String, url: String)
    fun deletePatient(patient: Patient)

    fun getRecords(idPatient: String): Flow<List<Record>>
    fun getRecord(id: String): Flow<Record>
    fun insertRecord(record: Record)
    fun updateRecord(record: Record)
    fun deleteRecord(id: String)

    fun getStaffs(): Flow<List<Staff>>
    fun getStaffDetail(id: String): Flow<Resource<Staff>>
    fun getStaff(email: String): Flow<Resource<Staff>>
    suspend fun insertStaff(staff: Staff): String
    fun updateStaff(staff: Staff)
    fun updatePictureStaff(id: String, url: String)
    fun deleteStaff(staff: Staff)
}