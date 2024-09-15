import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Student {
    private int id;
    private String lastName;
    private String firstName;
    private String surName;
    private String DOB;
    private String address;
    private String phone;
    private String faculty;
    private int course;
    private String group;

    public Student(int id, String lastName, String firstName, String surName, String DOB, String address, String phone, String faculty, int course, String group) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.surName = surName;
        this.DOB = DOB;
        this.address = address;
        this.phone = phone;
        this.faculty = faculty;
        this.course = course;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student: " +
                "id = " + id +
                ", lastName = " + lastName +
                ", firstName = " + firstName +
                ", surName = " + surName +
                ", DOB = " + DOB +
                ", address= " + address +
                ", phone= " + phone +
                ", faculty= " + faculty +
                ", course= " + course +
                ", group= " + group;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, firstName, surName, DOB, address, phone, faculty, course, group);
    }


    // переопределяем, чтобы сравнивать поля объектов одного класса, а не сами объекты(их ссылки)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false; // проверяем принадлежность к одному классу
        Student student = (Student) o;
        return id == student.id &&
               course == student.course &&
               Objects.equals(lastName, student.lastName) &&
               Objects.equals(firstName, student.firstName) &&
               Objects.equals(surName, student.surName) &&
               Objects.equals(DOB, student.DOB) &&
               Objects.equals(address, student.address) &&
               Objects.equals(phone, student.phone) &&
               Objects.equals(faculty, student.faculty) &&
               Objects.equals(group, student.group);
    }
}

class StudentFilters {
    private final List<Student> students;

    public StudentFilters() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getStudentsByFaculty(String faculty) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getFaculty().equalsIgnoreCase(faculty)) { // сравниваем строки без учёта регистра
                result.add(student);
            }
        }
        return result;
    }

    public List<List<Student>> getStudentsByFacultyAndCourse() {
        List<String> faculties = new ArrayList<>();
        List<Integer> courses = new ArrayList<>();

        for (Student student : students) {
            if (!faculties.contains(student.getFaculty())) {
                faculties.add(student.getFaculty());
            }
            if (!courses.contains(student.getCourse())) {
                courses.add(student.getCourse()); 
            }
        }

        int fuculties_count = faculties.size();
        int courses_count = courses.size();

        List<List<Student>> result = new ArrayList<>();
        for (int i = 1; i <= fuculties_count; i++) { 
            for (int j = 1; j <= courses_count; j++) {
                List<Student> facultyCourseList = new ArrayList<>();
                for (Student student : students) {
                    if (student.getFaculty().equals("Faculty" + i) && student.getCourse() == j) {
                        facultyCourseList.add(student);
                    }
                }
                result.add(facultyCourseList);
            }
        }
        return result;
    }

    public List<Student> getStudentsByYearOfBirth(int year) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            int studentYear = Integer.parseInt(student.getDOB().split("\\.")[2]); // берём год по разделителю и возвращаем целое число
            if (studentYear > year) {
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> getStudentsByGroup(String group) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getGroup().equalsIgnoreCase(group)) {
                result.add(student);
            }
        }
        return result;
    }

    public void printStudents(List<Student> students) {
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void printStudentsLists(List<List<Student>> studentsLists) {
        for (List<Student> list : studentsLists) {
            for (Student student : list) {
                System.out.println(student);
            }
        }
    }
}

public class main {
    public static void main(String[] args) {
        StudentFilters StudentFilters = new StudentFilters();

        // Добавление студентов
        StudentFilters.addStudent(new Student(1, "Smith", "John", "A.", "01.01.1995", "Address1", "123456789", "Faculty1", 1, "GroupA"));
        StudentFilters.addStudent(new Student(2, "Doe", "Jane", "B.", "02.02.1996", "Address2", "987654321", "Faculty2", 2, "GroupB"));
        StudentFilters.addStudent(new Student(3, "Johnson", "Mike", "C.", "03.03.1997", "Address3", "111222333", "Faculty1", 2, "GroupA"));
        StudentFilters.addStudent(new Student(4, "Williams", "Anna", "D.", "04.04.1998", "Address4", "444555666", "Faculty3", 2, "GroupA"));
        StudentFilters.addStudent(new Student(5, "Brown", "David", "E.", "05.05.1999", "Address5", "777888999", "Faculty3", 3, "GroupB"));
        StudentFilters.addStudent(new Student(6, "Brown", "David", "E.", "05.05.1999", "Address5", "777888999", "Faculty1", 1, "GroupB"));

        // 1) Список студентов заданного факультета
        List<Student> faculty1Students = StudentFilters.getStudentsByFaculty("Faculty1");
        System.out.println("Студенты факультета Faculty1:");
        StudentFilters.printStudents(faculty1Students);

        // 2) Списки студентов для каждого факультета и курса
        List<List<Student>> facultyCourseStudents = StudentFilters.getStudentsByFacultyAndCourse();
        System.out.println("\nСтуденты по факультетам и курсам:");
        StudentFilters.printStudentsLists(facultyCourseStudents);

        // 3) Список студентов, родившихся после заданного года
        List<Student> studentsAfter1997 = StudentFilters.getStudentsByYearOfBirth(1997);
        System.out.println("\nСтуденты, родившиеся после 1997 года:");
        StudentFilters.printStudents(studentsAfter1997);

        // 4) Список учебной группы
        List<Student> groupAStudents = StudentFilters.getStudentsByGroup("GroupA");
        System.out.println("\nСтуденты группы GroupA:");
        StudentFilters.printStudents(groupAStudents);
    }
}
