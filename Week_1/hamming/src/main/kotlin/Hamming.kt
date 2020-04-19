import java.lang.IllegalArgumentException

object Hamming {

    fun compute(leftStrand: String, rightStrand: String): Int {
        // TODO("Implement this function to complete the task")
        if((leftStrand.length == 0) or (rightStrand.length == 0)) {
            return 0
        }

        if(leftStrand.length != rightStrand.length) {
            throw IllegalArgumentException("left and right strands must be of equal length")
        }

        var counter:Int = 0
        for(i in 0..(leftStrand.length - 1)) {
            if(leftStrand[i] != rightStrand[i])
                counter ++
        }
        return counter
    }
}
