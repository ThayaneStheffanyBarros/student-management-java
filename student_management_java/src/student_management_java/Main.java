package student_management_java;

import java.sql.*;
import java.util.Scanner;

public class Main {
	
	private static final String JBDC_URL = "jdbc:h2:mem:testdb";
	private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    
	public static void main(String[] args) {
		System.out.println("Entrei");
		try {
			Connection connection = DriverManager.getConnection(JBDC_URL, USERNAME, PASSWORD);
			createTable(connection);
			
			Scanner scanner = new Scanner(System.in);
			boolean exit = false;
			
			do {
				System.out.println("\nEscolha uma opção:");
                System.out.println("1. Adicionar novo aluno");
                System.out.println("2. Consultar alunos");
                System.out.println("3. Atualizar aluno");
                System.out.println("4. Excluir aluno");
                System.out.println("5. Consultar aluno por ID");
                System.out.println("6. Sair");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch(choice) {
                case 1:
                	addStudent(connection, scanner);
                	break;
                case 2:
                	listStudents(connection);
                	break;
                case 3:
                	updateStudents(connection, scanner);
                	break;
                case 4:
                	deleteStudent(connection, scanner);
                	break;
                case 5:
                	findStudentById(connection, scanner);
                	break;
                case 6:
                	exit = true;
                	System.out.println("Saindo ...");
                	break;
                default:
                	System.out.println("Opção inválida, scolha novamente.");
                    break;
                }
				
			}while(!exit);
			
			connection.close();
			scanner.close();
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void findStudentById(Connection connection, Scanner scanner) {
		System.out.print("Digite o ID do aluno que deseja consultar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    System.out.println("Aluno encontrado - ID: " + id + ", Nome: " + name + ", Idade: " + age);
                } else {
                    System.out.println("Aluno com ID " + id + " não encontrado.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar aluno por ID ");
        }
		
	}

	private static void deleteStudent(Connection connection, Scanner scanner) {
		System.out.print("Digite o ID do aluno que deseja excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " linha(s) excluída(s) para ID " + id);
        } catch (SQLException e) {
            System.out.println("Erro ao excluir aluno ");
        }
		
	}

	private static void updateStudents(Connection connection, Scanner scanner) {
		System.out.print("Digite o ID do aluno que deseja atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Digite o novo nome do aluno: ");
        String name = scanner.nextLine();

        System.out.print("Digite a nova idade do aluno: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE students SET name = ?, age = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setInt(3, id);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " linha(s) atualizada(s) para ID " + id + ": " + name + ", " + age);
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar aluno");
        }
		
	}

	private static void listStudents(Connection connection) {
		String sql = "SELECT * FROM students";
        try {
        	Statement statement = connection.createStatement();
    		ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("\nLista de alunos:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                System.out.println("ID: " + id + ", Nome: " + name + ", Idade: " + age);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar alunos ");
        }
		
	}

	private static void addStudent(Connection connection, Scanner scanner) {
		System.out.println("Digite o nome do aluno: ");
		String name = scanner.nextLine();
		System.out.print("Digite a idade do aluno: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        
        String sql = "INSERT INTO students (name, age) values (?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
        	preparedStatement.setString(1, name);
        	preparedStatement.setInt(2, age);
        	
        	int rowsAffected = preparedStatement.executeUpdate();
        	System.out.println(rowsAffected + " linha(s) inserida(s) para: " + name + ", " + age);
        } catch (SQLException e) {
            System.out.println("Erro ao inserir o aluno");
        }
		
	}

	private static void createTable(Connection connection) {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS students (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age INT)";
		try(Statement statement = connection.createStatement()){
			statement.execute(createTableSQL);
			System.out.println("Tabela criada com sucesso!");
		} catch(SQLException e) {
			System.out.println("Erro ao criar tabela");	
		}
		
	}
}
