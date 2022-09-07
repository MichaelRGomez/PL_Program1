class languageRecognizer constructor(input : String) {
    //coverting the input string in to a list of type string
    private var workingSentence : List<String> = input.split(Regex("\\s+"))

    //This function must start with: flag = 0, errflag = false, and index = 0
    public var hasError : Boolean = checkSentence(workingSentence, 0, false, 0)

    private fun checkSentence(sen : List<String>, flag : Int, errFlag : Boolean, index : Int): Boolean{
        var ierrFlag : Boolean = false
        /*
        Flags
           -1 - terminates the recursion
            0 - checks the edges of the string, as well as empty instruction
            1 - checks for the first variable
            2 - checks for the assignment variable
            3 - checks for the assignment variable and then peaks at the next operator / ; / stop lexemes
        */

        if(flag != -1){
            //Recursive flow control
            when(flag){
                -1 -> {
                    //ending the recursion
                    return errFlag
                }

                //checking the edges and for emtpy instruction
                0 -> {
                    //checking for start
                    if (sen.first() != "start") {
                        println("syntax error: expected \"start\" instead of \"" + sen.first() + "\"")
                        ierrFlag = true
                    }
                    //checking for stop
                    if (sen.last() != "stop") {
                        println("syntax error: expected \"stop\" instead of \"" + sen.last() + "\"")
                        ierrFlag = true
                    }
                    //checking for empty instructions
                    if (sen.get(1) == "stop") {
                        println("no instructions to compile")
                        return checkSentence(sen,-1, true, 0)
                    }

                    //next
                    return  checkSentence(sen, 1, (ierrFlag || errFlag), index + 1)
                }

                //checking for the first variable
                1 -> {
                    if(sen.get(index) != "stop" || sen.get(index) != sen.last())
                    {
                        if(sen.get(index) == "R" || sen.get(index) == "S" || sen.get(index) == "T" || sen.get(index) == "U" )
                        {
                            checkSentence(sen, 2, errFlag, index + 1)
                        }
                        else{
                            println("syntax error: expected \"R\",\"S\",\"T\",\"U\", instead of \"" + sen.get(index) + "\"")
                            ierrFlag = true
                            return checkSentence(sen, 2, (ierrFlag || errFlag), index + 1)
                        }
                    }
                }

                //checks the assignment token
                2 -> {
                    if(sen.get(index) != "stop" || sen.get(index) != sen.last())
                    {
                        if(sen.get(index) != "<=")
                        {
                            println("syntax error: expected \"<=\" instead of \"" + sen.get(index) + "\"")
                            ierrFlag = true
                        }
                        return checkSentence(sen, 3, (ierrFlag || errFlag), index + 1)
                    }
                }

                //checking the assigned variable
                3 -> {
                    if(sen.get(index) != "stop" || sen.get(index) != sen.last())
                    {
                        //checking if it follows syntax
                        if(sen.get(index) == "R" || sen.get(index) == "S" || sen.get(index) == "T" || sen.get(index) == "U" )
                        {
                            //next

                            //checking if next is a token, ; , or stop
                            if(sen.get(index + 1) == "/" || sen.get(index + 1) == "*")
                            {
                                checkSentence(sen, 3, errFlag, index + 2)
                            }
                            else if (sen.get(index + 1) == ";") {
                                checkSentence(sen, 1, errFlag, index + 2)
                            }
                            else if (sen.get(index + 1) == "stop"){
                                checkSentence(sen, -1, errFlag, index + 1)
                            }
                            //reporting that we don't know wtf is there
                            else{
                                println("syntax error: expected operation ( \"/\" or \"*\" ) or expected \";\" or  expected \"stop\"")
                                checkSentence(sen, 1, true, index + 1)
                            }

                            //end of next
                        }
                        //reporting that the variable doesn't follow the syntax
                        else{
                            println("syntax error: expected \"R\",\"S\",\"T\",\"U\", instead of \"" + sen.get(index) + "\"")
                            ierrFlag = true
                            return checkSentence(sen, 3, (ierrFlag || errFlag), index + 1)
                        }
                    }
                }
            }

        }

        return (ierrFlag || errFlag)
    }

    //returns the formatted List<String>
    public fun getSen() : List<String>{
        return workingSentence
    }
}