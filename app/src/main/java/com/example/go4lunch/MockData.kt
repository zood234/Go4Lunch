package com.example.go4lunch

class MockData {
    val mockDataUsers = arrayOfNulls<User>(10)
    val user1 = User("1","Anakin Skywalker","1@gmail.com","",false)
    val user2 = User("2","Luke Skywalker","2@gmail.com","",false)
    val user3 = User("3","Leia Organa","3@gmail.com","",false)
    val user4 = User("4","Han Solo","4@gmail.com","",false)
    val user5 = User("5","Ben Solo","5@gmail.com","",false)
    val user6 = User("6","Obi-Wan Kenobi","6@gmail.com","",false)
    val user7 = User("7","Mace Windu","7@gmail.com","",false)
    val user8 = User("8","Sheev Palpatine","8@gmail.com","",false)
    val user9 = User("9","Lando Calrissian","9@gmail.com","",false)
    val user10 = User("10","Jyn Erso","10@gmail.com","",false)


    init {
        //addMockData()
        mockDataUsers[0]= user1
        mockDataUsers[1]= user2
        mockDataUsers[2]= user3
        mockDataUsers[3]= user4
        mockDataUsers[4]= user5
        mockDataUsers[5]= user6
        mockDataUsers[6]= user7
        mockDataUsers[7]= user8
        mockDataUsers[8]= user9
        mockDataUsers[9]= user10
    }


fun addMockData(){
    mockDataUsers[0]= user1
    mockDataUsers[1]= user2
    mockDataUsers[2]= user3
    mockDataUsers[3]= user4
    mockDataUsers[4]= user5
    mockDataUsers[5]= user6
    mockDataUsers[6]= user7
    mockDataUsers[7]= user8
    mockDataUsers[8]= user9
    mockDataUsers[9]= user10
}





}