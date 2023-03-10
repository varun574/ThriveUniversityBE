package com.example.springboot.thriveuniversitybackend.Public.services;

import com.example.springboot.thriveuniversitybackend.Public.dtos.LoginDto;
import com.example.springboot.thriveuniversitybackend.Public.dtos.UpdateProfileDto;
import com.example.springboot.thriveuniversitybackend.Public.dtos.UserRegisterDto;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.OldPasswardDoNotMatchException;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserNotFoundException;
import com.example.springboot.thriveuniversitybackend.Public.models.User;
import com.example.springboot.thriveuniversitybackend.Public.repositories.UserRepository;
import com.example.springboot.thriveuniversitybackend.enums.AttachmentTypes;
import com.example.springboot.thriveuniversitybackend.enums.UserTypes;
import com.example.springboot.thriveuniversitybackend.attachment.FileService;
import com.example.springboot.thriveuniversitybackend.otp.OTPService;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.services.StudentService;
import com.example.springboot.thriveuniversitybackend.teacher.dtos.TeacherDto;
import com.example.springboot.thriveuniversitybackend.teacher.services.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.example.springboot.thriveuniversitybackend.utils.RandomStringGenerator.generateRandomString;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private OTPService otpService;

    public boolean isValidUser(LoginDto loginDto) {
        User user = repository.findByEmail(loginDto.getEmail());
        return user.getPassword().equals(loginDto.getPassword());
    }

    public User findUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void saveUser(UserRegisterDto userRegisterDto) {
        User user = modelMapper.map(userRegisterDto, User.class);
        user.setPassword(generateRandomString(48, 122, 8));
        repository.save(user);
    }

    public void updatePassword(String email, String oldPassword, String newPassword) {
        User user = repository.findByEmail(email);
        if(user.getPassword().equals(oldPassword)){
            user.setPassword(newPassword);
            repository.save(user);
        }
        else{
            throw new OldPasswardDoNotMatchException("Old Password do not match.");
        }
    }

    public Object updateProfile(String type, String email, UpdateProfileDto updateProfileDto) {
        User user = repository.findByEmail(email);
        if(type.equals(UserTypes.STUDENT.name())){
            StudentDto studentDto = modelMapper.map(updateProfileDto, StudentDto.class);
            StudentDto updatedStudentDto = studentService.updateProfileByUserId(studentDto, user.getId());
            return updatedStudentDto;
        }
        else if(type.equals(UserTypes.TEACHER.name())){
            TeacherDto studentDto = modelMapper.map(updateProfileDto, TeacherDto.class);
            TeacherDto updatedTeacherDto = teacherService.updateProfileByUserId(studentDto, user.getId());
            return updatedTeacherDto;
        }
        else {
            //admin
            return null;
        }
    }

    public void uploadProfilePicture(String email, MultipartFile multipartFile, String attachmentType) throws IOException {
        User user = repository.findByEmail(email);
        fileService.upload(multipartFile, user.getId().hashCode()+"_"+attachmentType);
    }

    public Object getProfile(String type, String email) {
        User user = repository.findByEmail(email);
        String profilePictureURL = "";
        try {
            profilePictureURL = fileService.download(user.getId().hashCode()+"_"+ AttachmentTypes.PROFILE_PICTURE.name());
        } catch (IOException | RuntimeException e) {
            profilePictureURL = null;
        }
        if(type.equals(UserTypes.STUDENT.name())){
            StudentDto studentDto = studentService.getStudentByUserId(user.getId());
            studentDto.setProfilePictureURL(profilePictureURL);
            return studentDto;
        }
        else if(type.equals(UserTypes.TEACHER.name())){
            TeacherDto teacherDto = teacherService.getTeacherByUserId(user.getId());
            teacherDto.setProfilePictureURL(profilePictureURL);
            return teacherDto;
        }
        else {
            //admin
            return null;
        }
    }

    public void sendOtpMail(String email) {
        User user = repository.findByEmail(email);
        if(user == null)
            throw new UserNotFoundException("User not found, please try again with valid roll number");
        String type = user.getType();
        String personalEmail = "";
        if(type.equals(UserTypes.STUDENT.name())){
            personalEmail = studentService.getPersonalEmailByUserId(user.getId());
        } else if (type.equals(UserTypes.TEACHER.name())) {
            personalEmail = teacherService.getPersonalEmailByUserId(user.getId());
        }
        else{
            //admin
        }
        otpService.sendOtpMail(email, personalEmail, user.getName());
    }

    public String getUserIdByEmail(String email) {
        User user = findUserByEmail(email);
        return user.getId();
    }

    public String getEmailById(String userId) {
        Optional<User> user = repository.findById(userId);
        return user.orElseThrow(() -> new UserNotFoundException("User Not found")).getEmail();
    }

}
