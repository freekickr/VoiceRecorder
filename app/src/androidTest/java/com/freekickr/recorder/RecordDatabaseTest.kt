package com.freekickr.recorder

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.freekickr.recorder.database.AppDatabase
import com.freekickr.recorder.database.daos.RecordDao
import com.freekickr.recorder.database.entities.RecordItem
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class RecordDatabaseTest {

    private lateinit var recordDao: RecordDao
    private lateinit var database: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        recordDao = database.recordDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testDb() {
        recordDao.insert(RecordItem())
        val getCount = recordDao.getCount()
        assertEquals(getCount, 1)
    }

}