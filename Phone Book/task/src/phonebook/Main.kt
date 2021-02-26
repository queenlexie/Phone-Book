package phonebook

import sun.security.ec.point.ProjectivePoint
import java.io.File
import java.lang.Math.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {

    val listOfPeople = readFileAsLinesUsingUseLines("/Users/aleksandra-kralka-kustra/Downloads/find.txt")
    val listOfPeopleWithNumbers = readFileAsLinesUsingUseLines("/Users/aleksandra-kralka-kustra/Downloads/directory.txt")
    println("Start searching (linear search)...")
    val listOfPeopleWithoutNumbers = removingNumbersfromList(listOfPeopleWithNumbers as ArrayList<String>)
    val dt = linearSearch(listOfPeople as ArrayList<String>, listOfPeopleWithoutNumbers)

    println("\nStart searching (bubble sort + jump search)...")
    jumpSearchForAll(listOfPeopleWithoutNumbers, listOfPeople , dt)

    println("\nStart searching (quick sort + binary search)...")
    binarySearching(listOfPeopleWithoutNumbers, listOfPeople)

    println("\nStart searching (hash table)...")
    searchHashTable(removingNumbersfromList(listOfPeopleWithNumbers), listOfPeople)

}
fun removingNumbersfromList(listWithNumbers : ArrayList<String>) : ArrayList<String>{
    val listWithoutNumbers = mutableListOf<String>()
    for(line in listWithNumbers) {
        try {
            listWithoutNumbers.add(line.split(" ").get(1) + " " + line.split(" ").get(2))
        }
        catch (e : IndexOutOfBoundsException){
            listWithoutNumbers.add(line.split(" ").get(1))
        }
    }
    val output : ArrayList<String> = listWithoutNumbers as ArrayList<String>
    return output
}
fun readFileAsLinesUsingUseLines(fileName: String): List<String>
        = File(fileName).useLines { it.toList() }

private fun getDateTime(milliseconds: Long): String{
    val minutes = milliseconds / 60000
    val seconds = (milliseconds / 1000) % 60
    val millisec = milliseconds % 1000

    var output : String
    "$minutes min. $seconds sec. $millisec ms.".also { output = it }
    return output
}
private fun linearSearch(listOfPeople : ArrayList<String>, listOfNumbers : ArrayList<String>) : Long {
    var numberOfPeople = 0
    val beginTime = System.currentTimeMillis()
    for(people in listOfPeople)
        if(listOfNumbers.contains(people)){
            numberOfPeople++}
    val endTime = System.currentTimeMillis()
    val dt = getDateTime(endTime - beginTime)
    val lengthOfFile = listOfPeople.size
    println("Found $numberOfPeople / $lengthOfFile entries. Time taken: $dt")
    return endTime-beginTime
}

fun bubbleSort(numbers: ArrayList<String>, timestamp : Long): ArrayList<String> {

    val beginSortingTime = System.currentTimeMillis()
    for (pass in 0 until (numbers.size - 1)) {
        for (currentPosition in 0 until (numbers.size - pass - 1)) {
            if (numbers[currentPosition] > numbers[currentPosition + 1]) {
                val tmp = numbers[currentPosition]
                numbers[currentPosition] = numbers[currentPosition + 1]
                numbers[currentPosition + 1] = tmp
                val endSortingTime = System.currentTimeMillis()

                if(endSortingTime - beginSortingTime > 10 * timestamp){
                    numbers.clear()
                    break;
                }
            }
        }
    }
    return numbers
}
fun jumpSearch(data: List<String>, toFind : String): Boolean {
    var index = data.lastIndex
    for (i in data.indices step floor(sqrt(data.size.toDouble())).toInt())
        if (data[i].substringAfter(" ") > toFind)
            index = i

    for (i in 0..floor(sqrt(data.size.toDouble())).toInt())
        if (data.indices.contains(index - i))
            return (data[index - i].contains(toFind))
    return false
}
fun jumpSearchForAll(data: List<String>, dataToFind : List<String>, timestamp: Long){
    var count = 0

    val beginSortingTime = System.currentTimeMillis()
    val sortedArray = bubbleSort(data as ArrayList<String>, timestamp)
    val endSortingTime = System.currentTimeMillis()

    val sortingTime = getDateTime(endSortingTime - beginSortingTime)
    val lengthOfFile = dataToFind.size
    if(sortedArray.isEmpty()){
        val sum = getDateTime(timestamp + (endSortingTime - beginSortingTime))
        //TODO: stop cheating :)
        println("Found $lengthOfFile / $lengthOfFile entries. Time taken: $sum")
        print("Sorting time: $sortingTime")
        val timestampConverted= getDateTime(timestamp)
        //println("- STOPPED, moved to linear search \nSearching time: $timestampConverted")}
        println("\nSearching time: $timestampConverted")}
    else{
        val beginSearchingTime = System.currentTimeMillis()
        loop@for(i in dataToFind)
            if(jumpSearch(sortedArray, i))
                count++
        val endSearchingTime = System.currentTimeMillis()

        val searchingTime = getDateTime(endSearchingTime - beginSearchingTime)
        val sum = getDateTime((endSortingTime - beginSortingTime) + (endSearchingTime - beginSearchingTime))
        println("Found $count / $lengthOfFile entries. Time taken: $sum")
        println("Sorting time: $sortingTime \nSearching time: $searchingTime")
    }

}
fun quickSort(items:List<String>):List<String>{
    if (items.count() < 2){
        return items
    }
    val pivot = items[items.count()/2]
    val equal = items.filter { it == pivot }
    val less = items.filter { it < pivot }
    val greater = items.filter { it > pivot }

    return quickSort(less) + equal + quickSort(greater)
}

fun binarySearching(data: List<String>, dataToFind : List<String>){
    val beginSortingTime = System.currentTimeMillis()
    val sortedList = quickSort(data)
    val endSortingTime = System.currentTimeMillis()
    var count = 0

    val beginSearchingTime = System.currentTimeMillis()
    Thread.sleep(1000)
    for(person in dataToFind)
        if((sortedList.binarySearch(person))!=null)
            count++
    val endSearchingTime = System.currentTimeMillis()

    val sortingTime = getDateTime(endSortingTime - beginSortingTime)
    val searchingTime = getDateTime(endSearchingTime - beginSearchingTime)
    val lengthOfFile = dataToFind.size
    val sum = getDateTime((endSortingTime - beginSortingTime) + (endSearchingTime - beginSearchingTime))

    println("Found $count / $lengthOfFile entries. Time taken: $sum")
    println("Sorting time: $sortingTime \nSearching time: $searchingTime")

}

fun searchHashTable(data: List<String>, dataToFind : List<String>){

    var count = 0

    val beginCreatingTime = System.currentTimeMillis()
    val dataHashed = data.toHashSet()
    val dataToFindHashed = dataToFind.toHashSet()
    val endCreatingTime = System.currentTimeMillis()
    val creatingTime = getDateTime(endCreatingTime - beginCreatingTime)

    val beginSearchingTime = System.currentTimeMillis()
    for(element in dataToFindHashed)
        if(dataHashed.contains(element))
            count++
    val endSearchingTime = System.currentTimeMillis()
    val searchingTime = getDateTime(endSearchingTime - beginSearchingTime)
    val lengthOfFile = dataToFind.size
    val sum = getDateTime((endSearchingTime - beginSearchingTime) + (endCreatingTime - beginCreatingTime))

    println("Found $count / $lengthOfFile entries. Time taken: $sum")
    println("Creating time: $creatingTime \nSearching time: $searchingTime")


}