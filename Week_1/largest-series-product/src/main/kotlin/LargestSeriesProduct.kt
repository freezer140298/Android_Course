import java.lang.Integer.parseInt
import java.lang.NumberFormatException
import java.util.ArrayList

class Series constructor(val input: String) {

    // TODO: Implement proper constructor
    init {
        // Check if Input is valid
        if(!isInputNumeric()){
            throw IllegalArgumentException()
        }
    }

    fun getLargestProduct(span: Int): Long? {
        //TODO("Implement this function to complete the task")

        // Check if Input is blank and span = 0
        if((input.isEmpty()) and (span == 0)) {
            return 1
        }

        // Check if span is valid or input is empty
        if(isInputEmpty() or !isSpanValid(span) or (span > input.length)) {
            throw IllegalArgumentException()
        }


        if(input.length == span) {
            var product: Long = 1
            for(i in 0 until input.length) {
                var tmpChar: Char = input[i]
                var tmpNum : Int = tmpChar.getNumericValue()
                product *= tmpNum
            }
            return product
        }

        var products:MutableList<Long> = mutableListOf()

        for(i in (0 until input.length - span + 1)) {
            var tmpProduct: Long = 1
            for(j in i until i + span) {
                var tmpChar: Char = input[j]
                var tmpNum : Int = tmpChar.getNumericValue()
                tmpProduct *= tmpNum
            }
            products.add(tmpProduct)
        }

        return products.max()
    }


    private fun isInputNumeric(): Boolean {
        if (input.isEmpty())
            return true
        var numeric: Boolean = true
        numeric = input.matches("-?\\d+(\\.\\d+)?".toRegex())
        return numeric
    }

    private fun isInputEmpty(): Boolean {
        if(input.isEmpty()) {
            return true
        }
        return false
    }

    fun isSpanValid(span: Int): Boolean {
        if(span < 0)
        {
            return false
        }
        return true
    }

    fun Char.getNumericValue(): Int {
        if (!isDigit()) {
            throw NumberFormatException()
        }
        return this.toString().toInt()
    }
}
