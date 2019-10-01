package yummyloop.example.util.data

import com.mojang.datafixers.DataFixer
import net.minecraft.SharedConstants
import net.minecraft.datafixers.DataFixTypes
import net.minecraft.datafixers.Schemas
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtIo
import net.minecraft.server.MinecraftServer
import net.minecraft.util.TagHelper
import net.minecraft.world.WorldSaveHandler
import yummyloop.example.config.Config
import java.io.*
import java.util.function.Supplier
import kotlin.experimental.and
import kotlin.experimental.or

object LevelDataManager {
    private var dataFixer = Schemas.getFixer()
    private var worldDir : File? = null
    private var firstIni = true

    @JvmStatic // Might get called twice (client+server)
    fun ini(dir : File, server : MinecraftServer?, fixer : DataFixer){
        if (!firstIni) return
        firstIni = false
        setWorldDir(dir)
        setDataFixer(fixer)

    }

    private fun setWorldDir(file : File){
        this.worldDir=file
    }

    private fun setDataFixer(fixer : DataFixer){
        this.dataFixer=fixer
    }



/*
    fun getOrCreateState(supplier: Supplier<LevelData>, string_1: String): LevelData {
        val persistentState_1 = LevelDataManager.method_20786(supplier, string_1)
        if (persistentState_1 != null) {
            return persistentState_1
        } else {
            val persistentState_2 = supplier.get() as PersistentState
            //this.set(persistentState_2)
            return persistentState_2
        }
    }
*/
    private fun readState(file : File, supplier : Supplier<LevelData>) : LevelData?{
        try {
            if (file.exists()) {
                val levelData = supplier.get()
                val compoundTag = parseFile(file, SharedConstants.getGameVersion().worldVersion)
                levelData.fromTag(compoundTag.getCompound("data"))
                return levelData
            }
        } catch (e: Exception) {
            val name = file.name
            Config.logger.error("Error loading saved data: $name{$e}")
        }

        return null
    }

    @Throws(IOException::class)
    fun parseFile(file: File, worldVersion: Int): CompoundTag {
        val pushbackInputStream = PushbackInputStream(FileInputStream(file), 2)
        var exception: Throwable? = null

        val data: CompoundTag
        try {
            val rawData: CompoundTag
            if (this.byteCheck(pushbackInputStream)) {
                rawData = NbtIo.readCompressed(pushbackInputStream)
            } else {
                val dataInputStream = DataInputStream(pushbackInputStream)
                var inputException: Throwable? = null

                try {
                    rawData = NbtIo.read(dataInputStream)
                } catch (e: Throwable) {
                    inputException = e
                    throw e
                } finally {
                    if (dataInputStream != null) {
                        if (inputException != null) {
                            try {
                                dataInputStream.close()
                            } catch (e: Throwable) {
                                inputException.addSuppressed(e)
                            }
                        } else {
                            dataInputStream.close()
                        }
                    }
                }
            }

            val dataVersion = if (rawData.containsKey("DataVersion", 99)) rawData.getInt("DataVersion") else 1343
            data = TagHelper.update(this.dataFixer, DataFixTypes.SAVED_DATA, rawData, dataVersion, worldVersion)
        } catch (e: Throwable) {
            exception = e
            throw e
        } finally {
            if (pushbackInputStream != null) {
                if (exception != null) {
                    try {
                        pushbackInputStream.close()
                    } catch (e: Throwable) {
                        exception.addSuppressed(e)
                    }
                } else {
                    pushbackInputStream.close()
                }
            }
        }
        return data
    }

    @Throws(IOException::class)
    private fun byteCheck(pushbackInputStream: PushbackInputStream): Boolean {
        val bytes = ByteArray(2)
        var check = false
        val streamRead = pushbackInputStream.read(bytes, 0, 2)
        if (streamRead == 2) {
            val checkKey = (((bytes[1] and 255.toByte()).toInt() shl 8).toByte() or (bytes[0] and 255.toByte())).toInt()
            if (checkKey == 35615) {
                check = true
            }
        }

        if (streamRead != 0) {
            pushbackInputStream.unread(bytes, 0, streamRead)
        }

        return check
    }
}


fun WorldSaveHandler.saveState(data : LevelData) {
    val name = data.getName()
    try {
        val newFile = File(this.worldDir, "$name.dat_new")
        val oldFile = File(this.worldDir, "$name.dat_old")
        val defaultFile = File(this.worldDir, "$name.dat")
        //write data stuff to new file
        data.save(newFile)

        if (oldFile.exists()) {
            oldFile.delete()
        }

        defaultFile.renameTo(oldFile)
        if (defaultFile.exists()) {
            defaultFile.delete()
        }

        newFile.renameTo(defaultFile)
        if (newFile.exists()) {
            newFile.delete()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/*
fun WorldSaveHandler.loadState(data : LevelData): LevelData? {
    val name = data.getName()
    var defaultFile = File(this.worldDir, "$name.dat")
    if (defaultFile.exists()) {
        val state = LevelDataManager.readState(defaultFile)
        if (state != null) {
            return state
        }
    }

    defaultFile = File(this.worldDir, "$name.dat_old")
    return if (defaultFile.exists()) {
        LevelDataManager.readState(defaultFile)
    } else {
        null
    }
}
*/