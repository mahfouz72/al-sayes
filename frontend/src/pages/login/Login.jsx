import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../../APIs/loginUser";
import Password from '../../components/password/Password';
import '../../styles/form.css'

function LoginForm(){

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [usernameError, setUsernameError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const navigate = useNavigate();

    const handleSubmit = async () => {
        setUsernameError("");
        setPasswordError("");
        setErrorMessage("");

        if(username === ''){
            setUsernameError("Username is required!");
            return;
        }

        if(username.length < 3){
            setUsernameError("The username must be at least 3 characters");
            return;
        }

        if(password === ''){
            setPasswordError("Password is required!");
            return;
        }

        if(password.length < 8){
            setPasswordError("The password must be at least 8 characters");
            return;
        }

        if(password.length > 128){
            setPasswordError("The password must be at most 128 characters");
            return;
        }

        console.log("Login attempt with:", { username, password });

        const userData = {
            username: username,
            password: password,
        };

        try {
            const response = await loginUser(userData); 

            if(response.status === 200){
                const token = await response.text();
                localStorage.setItem("token", token); //to be improved later
                console.log("Login ssuccessfully");
                navigate('/');
            }
            else{
                const errorMessage = await response.text();
                console.log(errorMessage);
                setErrorMessage("Invalid username or password");
            }

        } catch (error) {
            console.log("something went wrong");
            setErrorMessage("Something went wrong, Try again later!");
        }

    };

    return(
        <div className="wrapper">
            <form action="">
                <h1>Login</h1>

                <div className="input-box">
                    <input type="text" 
                        placeholder="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <p className="error">{usernameError}</p>
                </div>

                <div className="input-box">
                <Password
                    value={password}
                    setValue={setPassword}
                    placeholder="password"
                />
                    <p className="error">{passwordError}</p>
                </div>

                <div className="forget">
                    <a href="#1">Forget password</a>
                </div>
                <p className="error">{errorMessage}</p>
                <button className="btn" type="button" onClick={handleSubmit}>Log in</button>
                <div className="register">
                    <p>Don't have an account? <Link to="/signup">Register</Link></p>
                </div>
            </form>
        </div>
    )
}

export default LoginForm;