class languageRecognizer constructor(input : String) {
    //coverting the input string in to a list of type string
    private var workingSentence : List<String> = input.split(Regex("\\s+"))

    public var hasError : Boolean = sentenceValidation(workingSentence)

    //checking if the workingSentence is a valid input\
    private fun sentenceValidation(sen : List<String>) : Boolean {
        var hasError : Boolean = false
        //checking for start
        if(sen.first() != "start"){
            println("syntax error: expected \"start\" instead of \"" + sen.get(0) + "\"")
            hasError = true
        }
        //checking for end
        if(sen.last() != "stop"){
            println("syntax error: expected \"stop\" instead of \"" + sen.get(sen.size - 1) + "\"")
            hasError = true
        }
        //checking for empty body
        if (sen.get(1) == "stop"){
            println("logic error: nothing between \"start\" and \"end\"")
            hasError = true
        }

        //checking the instruction syntax
        hasError = instructionValidation(sen)

        //end of function
        return hasError
    }

    private fun instructionValidation(sen : List<String>): Boolean{
        //return variable
        var hasError = false

        //checking if they are multiple instructions
        var iAmount : Int = instructionAmount(sen)
        var iRange : Int = sen.size - 2
        var iFlag : Int = 0
        var i : Int = 1
        /*
        Flag List:
       -1 : break loop
        0 : start of instruction
        1 : assignment identifier
        2 : 1st variable
        3 : check for identifier
        4 : checking for stop
        */


        //checking if the instructions are valid
        while(iRange != i) {
            when (iFlag) {
                //end of instruction loop
                -1 -> break

                //checking starting variable
                0 -> if (checkVariable(sen.get(i))) {
                    println("syntax error: expected variable: \"R\" , \"S\" , \"T\" , \"U\"  instead of \"" + sen.get(i) + "\"")
                    hasError = true
                } else {
                    iFlag = 1
                }

                //checking assignment identifier
                1 -> if (sen.get(i) != "<=") {
                    println("syntax error: expected assignment identifier: \"<=\" instead of \"" + sen.get(i) + "\"")
                    hasError = true
                } else {
                    iFlag = 2
                }

                //checking 2nd variable
                2 -> if (checkVariable(sen.get(i))) {
                    println("syntax error: expected variable: \"R\" , \"S\" , \"T\" , \"U\"  instead of \"" + sen.get(i) + "\"")
                    hasError = true
                } else {
                    iFlag = 3
                }

                //checking if an operation is occuring
                3 -> if (sen.get(i) == "/" || sen.get(i) == "*") {
                    iFlag = 2

                    //checking if the semicolor terminal is being used
                } else if (sen.get(i) == ";"){
                    iAmount--
                    iFlag = 0

                    //checking if anything it left to check
                } else if (sen.get(i + 1) == "stop"){
                    println("syntax error: incorrect format, either extry varaible or not-terminal")
                    hasError = true
                    break

                    //checking the last error case ¯\_(ツ)_/¯
                } else if (iAmount > 0){
                     println("syntax error: incorrect terminator expected \";\" instead of \"" + sen.get(i) + "\"")
                    hasError = true
                    iFlag = 0
                }
            }
            i++
        }
        return hasError
    }

    private fun instructionAmount(s : List<String>) : Int{
        var semicolonCount : Int = 0
        for (i in s)
        {
            if(i == ";"){
                semicolonCount++
            }
        }
        return semicolonCount
    }

    private fun checkVariable(v : String): Boolean {
        if(v == "R" || v == "S" || v == "T" || v == "U" ){
            return false
        }

        return true
    }


}