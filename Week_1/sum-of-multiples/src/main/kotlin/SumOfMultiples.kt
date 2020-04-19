object SumOfMultiples {

    fun sum(factors: Set<Int>, limit: Int): Int {
        //TODO("Implement this function to complete the task")

        // All number in factors are less than limit

        if(isFactorsValid(factors, limit) == 0) {
            return 0
        }

        var multiSet:MutableSet<Int> = mutableSetOf()
        for(factor in factors) {
            if(factor == 0)
                continue
            multiplesOfFactor(factor, limit, multiSet)
        }
        return multiSet.sum()
}
    fun isFactorsValid(factors: Set<Int>, limit: Int): Int {
        var counter: Int = 0
        for(factor in factors) {
            if (factor < limit) {
                counter++;
            }
        }
        return counter
    }

    fun multiplesOfFactor(factor : Int, limit: Int, multiplesSet: MutableSet<Int>) {
        if (factor > limit) {
            return
        }

        multiplesSet.add(factor)
        var i: Int = 2

        while (true) {
            if ((factor * i) < limit) {
                multiplesSet.add(factor * i)
                i++
            } else {
                break
            }
        }
    }
}
