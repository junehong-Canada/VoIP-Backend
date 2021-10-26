package skychatuc.io.proxyserver.sip;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skychatuc.io.proxyserver.Utils;

import javax.validation.Valid;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Slf4j
@RestController
public class SubscriberController {
    @Autowired
    private SubscriberRepository subscriberRepository;

    @PostMapping("/sipuser/add")
//    public Subscriber addUser(@Valid @RequestBody Subscriber subscriberDetails) throws NoSuchAlgorithmException {
    public Map< String, Boolean > addUser(@Valid @RequestBody Subscriber subscriberDetails) throws NoSuchAlgorithmException {
        if(subscriberRepository.existsByUsername(subscriberDetails.getUsername())) {
            Map < String, Boolean > response = new HashMap< >();
            response.put("exist username", Boolean.FALSE);
            return response;
        }
        Subscriber newSubscriber = new Subscriber();
        newSubscriber.setUsername(subscriberDetails.getUsername());
        newSubscriber.setDomain(subscriberDetails.getDomain());
        newSubscriber.setPassword(subscriberDetails.getPassword());
        newSubscriber.setFcmId(subscriberDetails.getFcmId());

        String ha1_ = subscriberDetails.getUsername() + ":" + subscriberDetails.getDomain() + ":" + subscriberDetails.getPassword();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(ha1_.getBytes());
        byte[] digest = md.digest();
        String ha1 = Utils.convertByteToHexadecimal(digest);
        newSubscriber.setHa1(ha1);

        String ha1b_ = subscriberDetails.getUsername() + "@" + subscriberDetails.getDomain() + ":" + subscriberDetails.getDomain() + ":" + subscriberDetails.getPassword();
        md.update(ha1b_.getBytes());
        digest = md.digest();
        String ha1b = Utils.convertByteToHexadecimal(digest);
        newSubscriber.setHa1b(ha1b);

        subscriberRepository.save(newSubscriber);

        Map < String, Boolean > response = new HashMap< >();
        response.put("added", Boolean.TRUE);
        return response;
//        return subscriberRepository.save(newSubscriber);
    }

    @GetMapping("/sipuser/users")
    public List< Subscriber > getAllUsers() {
        return subscriberRepository.findAll();
    }

    @GetMapping("/sipuser/user/{username}")
    public ResponseEntity< Subscriber > getUserByName(@PathVariable(value = "username") String username)
            throws ResourceNotFoundException {
        log.info("/sipuser/user/{username}");
        Subscriber subscriber = subscriberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found for this username :: " + username));
        return ResponseEntity.ok().body(subscriber);
    }

    @GetMapping("/sipuser/user")
    public ResponseEntity < Subscriber > getUserByName_(@Valid @RequestBody Subscriber subscriberDetails)
            throws ResourceNotFoundException {
        log.info("/sipuser/user");
        Subscriber subscriber = subscriberRepository.findByUsername(subscriberDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found for this username :: " + subscriberDetails.getUsername()));
        return ResponseEntity.ok().body(subscriber);
    }

    @PutMapping("/sipuser/update")
    public Map<String, Boolean> updateUser(@Valid @RequestBody Subscriber subscriberDetails)
            throws ResourceNotFoundException, NoSuchAlgorithmException {
        Subscriber updateSubscriber = subscriberRepository.findByUsername(subscriberDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found for this username :: " + subscriberDetails.getUsername()));

        updateSubscriber.setUsername(subscriberDetails.getUsername());
        updateSubscriber.setDomain(subscriberDetails.getDomain());
        updateSubscriber.setPassword(subscriberDetails.getPassword());
        updateSubscriber.setFcmId(subscriberDetails.getFcmId());

        String ha1_ = subscriberDetails.getUsername() + ":" + subscriberDetails.getDomain() + ":" + subscriberDetails.getPassword();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(ha1_.getBytes());
        byte[] digest = md.digest();
        String ha1 = Utils.convertByteToHexadecimal(digest);
        updateSubscriber.setHa1(ha1);

        String ha1b_ = subscriberDetails.getUsername() + "@" + subscriberDetails.getDomain() + ":" + subscriberDetails.getDomain() + ":" + subscriberDetails.getPassword();
        md.update(ha1b_.getBytes());
        digest = md.digest();
        String ha1b = Utils.convertByteToHexadecimal(digest);
        updateSubscriber.setHa1b(ha1b);

        final Subscriber updatedSubscriber = subscriberRepository.save(updateSubscriber);

        Map<String, Boolean> response = new HashMap< >();
        response.put("updated", Boolean.TRUE);
        return response;
    }

    @DeleteMapping("/sipuser/delete/{username}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "username") String username)
            throws ResourceNotFoundException {
        Subscriber foundSubscriber = subscriberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found for this id :: " + username));

        subscriberRepository.delete(foundSubscriber);
        Map <String, Boolean> response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @DeleteMapping("/sipuser/delete")
    public Map<String, Boolean> deleteUser_(@Valid @RequestBody Subscriber subscriberDetails)
            throws ResourceNotFoundException {
        Subscriber foundSubscriber = subscriberRepository.findByUsername(subscriberDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found for this id :: " + subscriberDetails.getUsername()));

        subscriberRepository.delete(foundSubscriber);
        Map<String, Boolean> response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
