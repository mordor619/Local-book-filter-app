# Local-book-filter-app

The application must do GET query once, write all book data to local Room/SQL storage, and then operate with the local SQL storage only.

Implement:
1. The application that downloads these data (from the http endpoint). After the data is downloaded using http, data must be saved into the local Room/SQL database.
2. You must create Room/SQL database containing at least 2 entities: (1) Author, (2) Book.
3. Create int primary key id for both Author and Book entities.
4. Book must have int foreign key referencing Author entity (in the model of this task, one book has one author, one author may have multiple books).
5. Populate Author and Book entities using the data obtained from http response (GET (api link))
6. Define queries that return Book entities by Author name.
7. Change GUI filters, so there's at least one 'by Author' filter.
8. When user updates filter information in GUI, query all books from the local database written by queried author.
9. When filtering criteria is applied, top 3 result books must be displayed using the TextView. Also display primary ids of the result books.


Some things that i considered-> 
1. title of the books should be unique.
2. author name should be unique
3. foreign key has been used and book table has authId column to reference
4. co-routines have been used
