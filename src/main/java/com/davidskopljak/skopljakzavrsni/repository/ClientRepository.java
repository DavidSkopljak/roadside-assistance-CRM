package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Client;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ClientRepository extends AbstractRepository<Client> {
    private static final ReentrantLock LOCK = new ReentrantLock(true);

    @Override
    public synchronized Client findById(Long id) throws SQLException {
        LOCK.lock();

        String sql = "SELECT client.id, client.first_name, client.last_name, client.contact_number FROM client WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    return new Client(id, rs.getString("first_name"), rs.getString("last_name"), rs.getString("contact_number"));
                }else{
                    throw new EmptyResultSetException("Client with id " + id + " not found");
                }
            }

        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public synchronized List<Client> findAll() throws SQLException {
        LOCK.lock();
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT client.id, client.first_name, client.last_name, client.contact_number FROM client WHERE 1 = 1";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()) {
                clients.add(new Client(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("contact_number")));
            }
            return clients;

        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
           LOCK.unlock();
        }

    }

    @Override
    public synchronized Long save(Client entity) throws SQLException {
      LOCK.lock();

       String sql = "INSERT INTO client (first_name, last_name, contact_number) VALUES (?, ?, ?) RETURNING id";
       try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

           ps.setString(1, entity.getFirstName());
           ps.setString(2, entity.getLastName());
           ps.setString(3, entity.getContactNumber());

           try(ResultSet rs = ps.executeQuery();){
               if (rs.next()) {
                   return rs.getLong("id");
               }else{
                   throw new EmptyResultSetException("No id retrieved for created client, possible issue with database");
               }
           }

       }catch(RepositoryAccessException e){
           throw new RepositoryAccessException(e.getMessage(), e);
       }finally {
           LOCK.unlock();
       }
    }


}
