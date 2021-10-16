import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient


import java.io.FileInputStream

// Initialize the Database -- Firestore
fun initializeDatabase(): Firestore? {
    val serviceAccount = FileInputStream("keepnote-dc727-firebase-adminsdk-otmho-f10f2b53f8.json")
    val credentials = GoogleCredentials.fromStream(serviceAccount)
    val options = FirebaseOptions.Builder().setCredentials(credentials).build()
    FirebaseApp.initializeApp(options)
    val db = FirestoreClient.getFirestore()
    return db
}

// Display all the Options that we have

fun displayOption()
{
    println("1. Create a Note")
    println("2. View Notes")
    println("3. Edit a Note")
    println("4. Delete a Note")
    println("5. Quit Program")

}
// Create Note
fun createNote(db: Firestore)
{
    print("Title: ")
    val title = readLine()
    print("Description: ")
    val description = readLine()
    print("Important (Y/N): ")
    val important = readLine()

    val querySnapshot = db.collection("Notes").document(title.toString()).get();
    val document = querySnapshot.get();

    if(document.exists())
    {
        println("The document name ${title} exists please use another one.")
    }
    // Use the Dictionary
    val data = mapOf("Title" to title, "Description" to description, "Important" to important);

    db.collection("Notes").document(title.toString()).set(data)

    println("Notes Added Successfully ✅ ✅")

}

// Edit the Note
fun editNote(db: Firestore)
{
    print("Please Enter the Title of the Note that you would like to edit: ")
    val editTitle = readLine()
    val querySnapshot = db.collection("Notes").whereEqualTo("Title", editTitle.toString()).get();
    val document = querySnapshot.get()
    // Check if it was empty or not
    if(document.isEmpty() )
    {
        println("The Note's doesn't exist")
        return
    }
    print("Enter a new Title: ")
    val newTitle = readLine()
    print("Enter a new Description: ")
    val newDescription = readLine()
    print("Enter Importance (Y/N)")
    val newImportance = readLine()

    db.collection("Notes").document(editTitle.toString()).delete();

    val data = mapOf("Title" to newTitle, "Description" to newDescription, "Important" to newImportance);

    db.collection("Notes").document(newTitle.toString()).set(data);

    println("Note Edited Successfully ✅✅")

}

// Delete The request Note
fun deleteNote(db:Firestore)
{
    print("Please Enter the title of the Note, that you would like to Delete: ")
    val title = readLine()

    val querySnapshot = db.collection("Notes").document(title.toString()).get()
    val document = querySnapshot.get();
    if(document.exists())
    {
        db.collection("Notes").document(title.toString()).delete();
        println("Document Deleted Successfully!!")
    } else {
        println("Document doesn't exist!! Please Try again.")
    }

}

// View All the Notes that we have stored in the database
fun viewNotes(db: Firestore)
{
    val querySnapshot = db.collection("Notes").get();
    var count  = 1;
    val document = querySnapshot.get();
    println("     Title                                Description                               Important")
    for(doc in document){
        println("${count}   ${doc.get("Title")} : ${doc.get("Description")} ---->  ${doc.get("Important")}")
        count+=1;
    }

}




fun main() {

      val db = initializeDatabase()

      var choice = 0;
      // Run the Program Until the user prefers to quit the program.
      while (choice != 5) {
          println(" ")
          displayOption()
          print("Please choose an option: ")
          choice =Integer.valueOf(readLine());

          if (choice == 1)
          {
              if (db != null) {
                  createNote(db)
              }
          }
          else if(choice == 2)
          {
              if(db !=null)
              {
                  viewNotes(db)
              }
          }
          else if(choice == 3)
          {
              if(db != null)
              {
                  editNote(db)
              }
          }
          else if(choice == 4)
          {
              if (db!= null)
              {
                  deleteNote(db)
              }
          }

      }


}