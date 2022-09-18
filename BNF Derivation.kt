class bnfDerivation constructor( fromatted_string : List<String>){
    //copy of the formatted string
    private var sen : MutableList<String> = fromatted_string.toMutableList()
    private var printingList : MutableList<String> = emptyList<String>().toMutableList()
    var index : Int = 1
    var programFlag : Boolean = false


    //public function that prints the derivation
    public fun print(){
        buildBNFDerivation()
    }

    //actual function that'll arrange the bnf derivation display
    private fun buildBNFDerivation(){
        addToPrinting() //adding the input string

        while(!programFlag){
            when(sen.get(index)){
                "R" -> rollVariable()
                "S" -> rollVariable()
                "T" -> rollVariable()
                "U" -> rollVariable()
               "<=" -> rollCalculation()
                ";" -> {
                    index++
                    rollVariable()
                }
             "stop" -> endDerivation()
            }
        }

        flipPrinting()

        var x : Int = 1
        println("<program> -> " + printingList[0])
        while(x != printingList.size){
            println( x.toString() + " -> " + printingList[x])
            x++
        }
    }
    //sub functions
    private fun endDerivation(){
        index--

        if(sen[index] == "<instruction>"){
            sen[index] = "<instructions>"
            addToPrinting()
            endDerivation()
        }else if (sen[index]  == "<instructions>"){
            sen.removeAt(index - 1)
            sen.removeAt(index - 1)
            addToPrinting()
            endDerivation()
        }
        else if (sen[index] == ";")
        {
            sen.removeAt(index - 1)
            sen.removeAt(index - 1)
            addToPrinting()

            index--

            endDerivation()
        }else {
            programFlag = true
        }
    }


    private fun rollCalculation(){
        if(sen[index + 2] == ";"  || sen[index + 2] == "stop"){//it's an assignment calculation
            sen[index + 1] = "<variable>"
            addToPrinting()

            sen[index + 1] = "<calculation>"
            addToPrinting()

            convertToInstruction()
        }else {
            sen[index + 1] = "<variable>"
            addToPrinting()

            sen[index + 3] = "<variable>"
            addToPrinting()

            sen.removeAt(index + 1)
            sen.removeAt(index + 1)
            sen.removeAt(index + 1)
            sen.add(index + 1, "<calculation>")
            addToPrinting()

            convertToInstruction()
        }
    }

    private fun convertToInstruction(){
        if(sen[index - 1] == "<variable>" && sen[index + 1] == "<calculation>"){
            sen.removeAt(index - 1)
            sen.removeAt(index - 1)
            sen.removeAt(index - 1)
            sen.add(index -1, "<instruction>")
            addToPrinting()
        }
    }

    private fun rollVariable(){
        sen[index] = "<variable>"
        addToPrinting()
        index++ //next
    }


    //helper functions
    private  fun cleanPrinting(){
        var temp : MutableList<String> = emptyList<String>().toMutableList()
        var s : String = ""
        for(i in printingList){
            temp.add(s)
        }
        printingList = temp
    }

    private fun flipPrinting(){
        var nes : MutableList<String> = emptyList<String>().toMutableList()
        for(i in printingList){
            nes.add(0, i)
        }
        printingList = nes
    }

    private fun addToPrinting(){
        var x : Int = 0
        var temp_s = ""
        while(x != sen.size){
            temp_s = temp_s + sen[x] + " "
            x++
        }
        printingList.add(temp_s)
    }
}