import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "../../APIs/userRegister";
import Password from '../../components/password/Password';
import '../../styles/form.css'

function LoginForm(){

    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [password, setPassword] = useState("");
    const [usernameError, setUsernameError] = useState("");
    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const navigate = useNavigate();

    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    const handleSubmit = async () => {
        setUsernameError("");
        setPasswordError("");
        setErrorMessage("");

        if(username === ''){
            setUsernameError("Username is required!");
            return;
        }

        if(username.length < 2){
            setUsernameError("The username must be at least 3 characters");
            return;
        }

        if(email === ''){
            setEmailError("Email is required!");
            return;
        }

        if(!isValidEmail(email)){
            setEmailError("Enter a valid email");
            return;
        }

        if(password === ''){
            setPasswordError("Password is required!");
            return;
        }

        if(password.length < 7){
            setPasswordError("The password must be at least 8 characters");
            return;
        }

        if(password.length > 128){
            setPasswordError("The password must be at most 128 characters");
            return;
        }

        if (!(password === confirmPassword)) {
            setPasswordError('Passwords do not match');
            return;
        }

        const userData = {
            username: username,
            email: email,
            password: password,
        };


        try {
            const response = await registerUser(userData);

            if(response.status === 200){
                console.log("Register successfully");
                navigate('/');
            }
            else{
                const errorMessage = await response.text();
                console.log(errorMessage);
                setErrorMessage("Error");
            }

        } catch (error) {
            console.log("something went wrong");
            setErrorMessage("Something went wrong, Try again later!");
        }
       
    };

    return(
        <div className="wrapper">
            <form action="">
                <h1>Signup</h1>

                <div className="input-box">
                    <input type="text" 
                        placeholder="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <p className="error">{usernameError}</p>
                </div>

                <div className="input-box">
                    <input type="email" 
                        placeholder="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    <p className="error">{emailError}</p>
                </div>

                <div className="input-box">
                <Password
                    value={password}
                    setValue={setPassword}
                    placeholder="password"
                />
                    <p className="error">{passwordError}</p>
                </div>

                <div className="input-box">
                <Password
                    value={confirmPassword}
                    setValue={setConfirmPassword}
                    placeholder="confirm password"
                />
                </div>
                
                <p className="error">{errorMessage}</p>
                <button type="button" className="btn" onClick={handleSubmit}>Create Account</button>
                <div className="register">
                    <p>Already have an account? <Link to="/login">login</Link></p>
                </div>
            </form>
        </div>
    )
}

export default LoginForm;