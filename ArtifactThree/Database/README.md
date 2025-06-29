
# CS-340 Client Server Development

## How do you write programs that are maintainable, readable, and adaptable? Especially consider your work on the CRUD Python module from Project One, which you used to connect the dashboard widgets to the database in Project Two. What were the advantages of working in this way? How else could you use this CRUD Python module in the future?

Understanding the project's objective from the beginning is essential for developing scalable, adaptable, and easy-to-maintain applications. 
Considering the development process of the CRUD module, working with different files is good practice as it helps structure the code according to various project scenarios.
For example, the animalsCRUD.py file contains the necessary methods to connect and manipulate data in the database. This facilitates future changes to connection parameters or the addition of new methods without affecting other project files. 
If, in the future, a technique for user creation from the interactive UI is needed, the module could simply be extended without modifying other parts of the code.
Another advantage of this organization is modularity, as each file has a specific function within the project. For instance, the testAnimalsCRUD.ipynb file allows verification of the CRUD module methods, 
ensuring that each function executes correctly before integrating it into the dashboard. This improves code quality, facilitates error detection, and makes the project easier to maintain in the future.

# How do you approach a problem as a computer scientist? Consider how you approached the database or dashboard requirements that Grazioso Salvare requested. How did your approach to this project differ from previous assignments in other courses? What techniques or strategies would you use in the future to create databases to meet other client requests?

Unlike other projects in previous courses, this project allowed me to develop a different working method by integrating a database with a programming language. Previously, I had worked with standalone programs, 
but this project taught me the importance of databases and how their integration with an application can optimize data access and organization.
A new aspect for me was using Jupyter Notebook as a development tool. I learned that this platform offers a more flexible alternative to a traditional IDE for projects that do not require large resources. Additionally, 
I learned strategies for designing a database that can handle large volumes of information in a structured and accessible way. In the case of the Grazioso Salvare project, the database allowed for efficient import, 
organization, and access to animal information. Creating an interactive web dashboard facilitated data visualization and analysis without the need to query the database manually.

# What do computer scientists do, and why does it matter? How would your work on this type of project help a company, like Grazioso Salvare, to do their work better?

Computer scientists play a fundamental role in developing and optimizing technological systems that improve efficiency and access to information within companies. In the case of Grazioso Salvare, 
this project enabled the development of an interactive web application that optimizes the management of rescue animal data, improving organization and facilitating decision-making.
This type of tool automates processes that would otherwise require manual data handling. For example, administrators can quickly access a visual dashboard displaying data in charts, 
interactive tables, and geolocated maps instead of reviewing paper documents or 
