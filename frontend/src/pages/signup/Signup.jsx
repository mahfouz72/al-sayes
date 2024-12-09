import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "../../APIs/userRegister";
import Password from '../../components/password/Password';
import '../../styles/form.css'

function LoginForm(){

    const [username, setUsername] = useState("");
    const [plateNumber, setPlateNumber] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [password, setPassword] = useState("");
    const [usernameError, setUsernameError] = useState("");
    const [plateNumberError, setplateNumberError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const navigate = useNavigate();

    function isValidplate(plateNumber) {
        const licenseRegex = /^[A-Z0-9-]{1,10}$/;
        return licenseRegex.test(plateNumber.toUpperCase());
    }

    const handleSubmit = async () => {
        setUsernameError("");
        setplateNumberError("");
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

        if(!isValidplate(plateNumber)){
            setplateNumberError("Invalid license number. Use letters, numbers, and hyphens only.");
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
            plateNumber: plateNumber,
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
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <p className="error">{usernameError}</p>
                </div>

                <div className="input-box">
                    <input type="text" 
                        placeholder="Car plate number"
                        value={plateNumber}
                        onChange={(e) => setPlateNumber(e.target.value)}
                    />
                    <p className="error">{plateNumberError}</p>
                </div>

                <div className="input-box">
                <Password
                    value={password}
                    setValue={setPassword}
                    placeholder="Password"
                />
                    <p className="error">{passwordError}</p>
                </div>

                <div className="input-box">
                <Password
                    value={confirmPassword}
                    setValue={setConfirmPassword}
                    placeholder="Confirm password"
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