package com.institutopacifico.actualidad.utils

import android.util.Base64
import com.orhanobut.logger.Logger
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream


/**
 * Created by mobile on 7/12/17.
 * Fernando Rubio Burga
 */

object CompressHelper {


  /*  fun compress(string: String): String {
        val os = ByteArrayOutputStream(string.length)
        val gos = GZIPOutputStream(os)
        gos.write(string.toByteArray(Charsets.US_ASCII))
        gos.close()
        val compressed = os.toByteArray()
        os.close()
        return String(compressed)
    }


    fun decompress(compressed: ByteArray): String {
        val bis = ByteArrayInputStream(compressed)
        val gis = GZIPInputStream(bis)
        val br = BufferedReader(InputStreamReader(gis, "US-ASCII"))
        val sb = StringBuilder()
        var line: String?=""
        while (line!=null) {
            line = br.readLine()
            sb.append(line)
        }
        br.close()
        gis.close()
        bis.close()
        return sb.toString()
    }


    fun decompress(compressed: String): String {
        return decompress(compressed.toByteArray(Charsets.UTF_8))
    }
*/

    fun compress(str: String): String {

        val blockcopy = ByteBuffer
                .allocate(4)
                .order(java.nio.ByteOrder.LITTLE_ENDIAN)
                .putInt(str.length)
                .array()
        val os = ByteArrayOutputStream(str.length)
        val gos = GZIPOutputStream(os)
        gos.write(str.toByteArray())
        gos.close()
        os.close()
        val compressed = ByteArray(4 + os.toByteArray().size)
        System.arraycopy(blockcopy, 0, compressed, 0, 4)
        System.arraycopy(os.toByteArray(), 0, compressed, 4,
                os.toByteArray().size)

        return String(Base64.encode(compressed,Base64.DEFAULT))

    }


    fun decompress(zipText: String): String {
        val compressed = Base64.decode(zipText,Base64.DEFAULT)
      /*  if (compressed.size > 4) {
            val gzipInputStream = GZIPInputStream(
                    ByteArrayInputStream(compressed, 4,
                            compressed.size - 4))

            val baos = ByteArrayOutputStream()
            var value = 0
            while (value != -1) {
                value = gzipInputStream.read()
                if (value != -1) {
                    baos.write(value)
                }
            }
            gzipInputStream.close()
            baos.close()
            val sReturn = String(baos.toByteArray())
            return sReturn
        } else {
            return ""
        }*/
      var output=  decompress(compressed)
        if(output.substring(output.length-4)=="null"){
            output=output.substring(0,output.length-4)
           //Logger.d(output)
        }
        return output
    }

    fun decompress(compressed: ByteArray): String {
        val bis = ByteArrayInputStream(compressed)
        val gis = GZIPInputStream(bis)
        val br = BufferedReader(InputStreamReader(gis, "UTF-8"))
        val sb = StringBuilder()
        var line: String?=""
        while (line!=null) {
            line = br.readLine()
            sb.append(line)
        }
        br.close()
        gis.close()
        bis.close()
        return sb.toString()
    }
}
