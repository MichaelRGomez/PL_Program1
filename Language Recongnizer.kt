import java.util.LinkedList

class languageRecognizer constructor(input : String) {
    //converting the input string in to a list of type string
    private var workingSentence : MutableList<String> = input.split(Regex("\\s+")).toMutableList()
    private var errorList : MutableList<String> = LinkedList()

    //This function must start with: flag = 0, errflag = false, and index = 0
    public var hasError : Boolean = checkSentence(workingSentence, 0, false, 0, errorList)

    private fun checkSentence(sen : MutableList<String>, flag : Int, errFlag : Boolean, index : Int, errL : MutableList<String>): Boolean{
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
                        errL.add("syntax error: expected \"start\" instead of \"" + sen.first() + "\"")
                        ierrFlag = true
                    }

                    //checking if the list only consists of one string
                    if(sen.size == 1){
                        errL.add("incomplete instruction")
                        return checkSentence(sen, -1, true, 0, errL)
                    }

                    //checking for stop
                    if (sen.last() != "stop") {
                        errL.add("syntax error: expected \"stop\" instead of \"" + sen.last() + "\"")
                        ierrFlag = true
                    }

                    //checking for empty instructions
                    if (sen.get(1) == sen.last()) {
                        errL.add("no instructions to compile")
                        return checkSentence(sen,-1, true, 0, errL)
                    }

                    //next
                    return  checkSentence(sen, 1, ierrFlag, index + 1,  errL)
                }

                //checking for the first variable
                1 -> {
                    //checking if the correct variable is being used
                    if(!isValidVariable(sen.get(index)))
                    {
                        errL.add("syntax error: expected \"R\",\"S\",\"T\",\"U\", instead of \"" + sen.get(index) + "\"")
                        ierrFlag = true
                    }

                    //check if the current string is 'stop' or if the next string is 'stop'
                    if(sen.get(index) == sen.last()){
                        errL.add("syntax error: expected another instruction but found " + sen.last() + " instead")
                        return checkSentence(sen, -1, true, index, errL)
                    }
                    if(isStopNext(sen.get(index + 1), sen.last()))
                    {
                        errL.add("syntax error: incomplete instruction")
                        return checkSentence(sen, -1, true, index + 1,  errL)
                    }

                    //next
                    return checkSentence(sen, 2, (ierrFlag || errFlag), index + 1, errL)
                }

                //checks the assignment token
                2 -> {
                    //checking for the assignment token
                    if(sen.get(index) != "<=")
                    {
                        errL.add("syntax error: expected \"<=\" instead of \"" + sen.get(index) + "\"")
                        ierrFlag = true
                    }

                    //check if the next string is 'stop'
                    if(isStopNext(sen.get(index + 1), sen.last()))
                    {
                        errL.add("syntax error: incomplete instruction")
                        return checkSentence(sen, -1, true, index + 1, errL)
                    }

                    //next
                    return checkSentence(sen, 3, (ierrFlag || errFlag), index + 1, errL)
                }

                //checking if assignment expression syntax is being followed
                3 -> {
                    //checks to see if ";" is stuck together with the variable
                    if(sen.get(index).length > 1  && sen.get(index) != sen.last()){
                        var copy : String = sen.get(index)
                        sen.removeAt(index)
                        sen.add(index, copy[1].toString())
                        sen.add(index, copy[0].toString())
                        return checkSentence(sen,3, errFlag, index, errL)
                    }

                    //checking for the first assigned variable
                    if(!isValidVariable(sen.get(index)))
                    {
                        errL.add("syntax error: expected \"R\",\"S\",\"T\",\"U\", instead of \"" + sen.get(index) + "\"")
                        ierrFlag = true
                    }

                    //next
                    return checkSentence(sen, 4, (ierrFlag || errFlag), index + 1, errL)
                }

                //checking if an expression token is being used
                4 -> {
                    //checks to see if ";" is stuck together with the variable
                    if(sen.get(index).length > 1  && sen.get(index) != sen.last()){
                        var copy : String = sen.get(index)
                        sen.removeAt(index)
                        sen.add(index, copy[1].toString())
                        sen.add(index, copy[0].toString())
                        return checkSentence(sen,4, errFlag, index, errL)
                    }

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
                            return checkSentence(sen, 3, errFlag, index + 1, errL)
                        }

                        //not a token or stop; something not following the syntax
                        -2 -> {
                            //reporting that we don't know wtf is there
                            errL.add("syntax error: expected operation ( \"/\" or \"*\" ) or expected \";\" or  expected \"stop\"")
                            errL.add("stopping compilation")
                            return checkSentence(sen, -1, true, index + 1, errL)
                        }

                        // completed assignment
                        1 -> return checkSentence( sen, 1, errFlag, index + 1, errL)

                        //normal assignment is preformed and instructions are completed
                        -1 -> return checkSentence(sen, -1, errFlag, index, errL)
                    }
                }
            }

        }

        if(errFlag) {
            println("Error(s) have been detected")
            for(i in errL){
                println(i)
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
}