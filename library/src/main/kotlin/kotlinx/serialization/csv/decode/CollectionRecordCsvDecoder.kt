package kotlinx.serialization.csv.decode

import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.csv.Csv

/**
 * Decode collection record.
 *
 * If the CSV record is a collection (list, set, map) the collection elements fill the whole line.
 * Therefore, the number of elements is determined by reading until the end of line and the size of the collection is
 * not required and consequently not expected as the first value.
 */
internal class CollectionRecordCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: RecordListCsvDecoder
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    private val recordNo = reader.recordNo

    override fun decodeElementIndex(desc: SerialDescriptor): Int = when {
        // TODO Check for END_OF_RECORD
        reader.isDone || reader.recordNo != recordNo -> CompositeDecoder.READ_DONE
        else -> elementIndex
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        super.endChildStructure(desc)
        elementIndex++
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        elementIndex++
        return value
    }
}
