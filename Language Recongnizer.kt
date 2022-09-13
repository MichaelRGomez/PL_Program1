class languageRecognizer constructor(input : String) {
    //coverting the input string in to a list of type string
    private var workingSentence : List<String> = input.split(Regex("\\s+"))

    //This function must start with: flag = 0, errflag = false, and index = 0
    public var hasError : Boolean = checkSentence(workingSentence, 0, false, 0)

    private fun checkSentence(sen : List<String>, flag : Int, errFlag : Boolean, index : Int): Boolean{
        var ierrFlag : Boolean = false
        /*
        Flags
           -1 - terminates the recursion and returns errFlag
            0 - checks the edges of the string, as well as empty instruction
            1 - checks for the variable
            2 - checks for the assignment token
            3 - checks for the first assigned variable
            4 - checks if an expression is being used
        */

        //checking for gibberish input
        for(i in sen){
            if (i.length > 5){
                println("gibberish has been detected, please read the syntax")
                return true
            }
        }

        if(flag != -1){
            //Recursive flow control
            when(flag){
                //checking the edges and for empty instruction
                0 -> {
                    //checking for start
                    if (sen.first() != "start") {
                        println("syntax error: expected \"start\" instead of \"" + sen.first() + "\"")
                        ierrFlag = true
                    }

                    //checking if the list only consists of one string
                    if(sen.size == 1){
                        println("incomplete instruction")
                        return checkSentence(sen, -1, true, 0)
                    }

                    //checking for stop
                    if (sen.last() != "stop") {
                        println("syntax error: expected \"stop\" instead of \"" + sen.last() + "\"")
                        ierrFlag = true
                    }

                    //checking for empty instructions
                    if (sen.get(1) == sen.last()) {
                        println("no instructions to compile")
                        return checkSentence(sen,-1, true, 0)
                    }

                    //next
                    return  checkSentence(sen, 1, ierrFlag, index + 1)
                }

                //checking for the first variable
                1 -> {
                    //checking if the correct variable is being used
                    if(!isValidVariable(sen.get(index)))
                    {
                        println("syntax error: expected \"R\",\"S\",\"T\",\"U\", instead of \"" + sen.get(index) + "\"")
                        ierrFlag = true
                    }

                    //check if the next string is 'stop'
                    if(isStopNext(sen.get(index + 1), sen.last()))
                    {
                        println("syntax error: incomplete instruction")
                        return checkSentence(sen, -1, true, index + 1)
                    }

                    //next
                    return checkSentence(sen, 2, (ierrFlag || errFlag), index + 1)
                }

                //checks the assignment token
                2 -> {
                    //checking for the assignment token
                    if(sen.get(index) != "<=")
                    {
                        println("syntax error: expected \"<=\" instead of \"" + sen.get(index) + "\"")
                        ierrFlag = true
                    }

                    //check if the next string is 'stop'
                    if(isStopNext(sen.get(index + 1), sen.last()))
                    {
                        println("syntax error: incomplete instruction")
                        return checkSentence(sen, -1, true, index + 1)
                    }

                    //next
                    return checkSentence(sen, 3, (ierrFlag || errFlag), index + 1)
                }

                //checking if assignment expression syntax is being followed
                3 -> {
                    //checking for the first assigned variable
                    if(!isValidVariable(sen.get(index)))
                    {
                        println("syntax error: expected \"R\",\"S\",\"T\",\"U\", instead of \"" + sen.get(index) + "\"")
                        ierrFlag = true
                    }

                    //next
                    return checkSentence(sen, 4, (ierrFlag || errFlag), index + 1)
                }

                //checking if an expression token is being used
                4 -> {
                    //checking which expression token is being used
                    /* flags
                       -2 = not following syntax
                       -1 = " stop " is the current lexeme
                        0 = " / " or " * " is the current token
                        1 = " ; " is the current token
                     */
                    when (whatExpressionToken(sen.get(index))){
                        //an expression is being used
                        0 -> {
                            return checkSentence(sen, 3, errFlag, index + 1)
                        }

                        //not a token or stop; something not following the syntax
                        -2 -> {
                            //reporting that we don't know wtf is there
                            println("syntax error: expected operation ( \"/\" or \"*\" ) or expected \";\" or  expected \"stop\"")
                            println("stopping compilation")
                            return checkSentence(sen, -1, true, index + 1)
                        }

                        // completed assignment
                        1 -> return checkSentence( sen, 1, errFlag, index + 1)

                        //normal assignment is preformed and instructions are completed
                        -1 -> return checkSentence(sen, -1, errFlag, index)
                    }
                }
            }

        }

        return errFlag
    }

    //internal helper functions
    private fun isStopNext(s : String, l : String) : Boolean {
        if (s == "stop" ||  s == l){
            return true
        }
        return false
    }

    private fun isValidVariable(s : String): Boolean{
        if (s == "R" || s == "S" || s == "T" || s == "U" )
        {
            return true
        }
        return false
    }

    private fun whatExpressionToken(s : String): Int{
        return when(s){
            "stop" -> -1
            "/" -> 0
            "*" -> 0
            ";" -> 1
            else -> -2
        }
    }

    //returns the formatted List<String>
    public fun getSen() : List<String>{
        return workingSentence
    }
}