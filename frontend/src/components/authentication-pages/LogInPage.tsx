import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import PasswordInput from "../../reusable-components/PasswordInput";
import TextInput from "../../reusable-components/TextInput";
import LoginData from "../../DTO/LoginDataDTO";
import User from "../../DTO/UserDTO";
import { LOGIN_URL } from "../../variable/constants";


type Props = {
    setIsAuthenticated: React.Dispatch<React.SetStateAction<boolean>>;
}

const LogInPage = (props: Props) => {
    return ( 
        <main className="loginPage panel-wrapper">
            <LogInSection setIsAuthenticated={props.setIsAuthenticated}/>
            <RegisterOptionSection />
        </main>
     );
}

const LogInSection = (props: Props) => {
    const [loginData, setLoginData] = useState<LoginData>({
        email: "",
        password: ""
    })

    const [wrongData, setWrongData] = useState(false);
    const navigate = useNavigate();

    const [user, setUser] = useState<User>();

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        fetch(LOGIN_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(loginData),
            credentials: "include"
        })
        .then ( resp => {
            if (resp.ok) {
                return resp.json()
            } else {
                throw Error()
            }    
        })
        .then ( data => {
            setUser(data)
        })
        .catch( error => setWrongData(true))
    }

    useEffect(() => {
        if (user !== undefined) {
            sessionStorage.setItem("id", user.id.toString())
            props.setIsAuthenticated(true)                

            navigate('/')
        }
    },[user])


    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {

        setLoginData(prevState => (
            {
                ...prevState,
                [e.target.name]: e.target.value
            }
        )) 
        
    }

    return (
        <section className="loginPage__login-section panel-wrapper__panel--no-image-background">
            <h2 className="panel-wrapper__title">Log In to Your Account</h2>
            <form className="form" onSubmit={(e) => handleSubmit(e)}>
                <div className="form__fields">
                    <TextInput 
                        name="email" 
                        state={loginData.email} 
                        setState={(e) => handleChange(e)} 
                        isRequired 
                        placeholder="Email"
                    />
                    <PasswordInput
                        name="password" 
                        state={loginData.password} 
                        setState={(e) => handleChange(e)} 
                        placeholder="Password" 
                     />            
                    {wrongData && <p className="error-text">Incorrect email or password</p>}
                </div>
                <button className="btn btn--pink btn--greater btn--greater-border-radius" type="submit">Log In</button>
            </form>
        </section>
    )
}


const RegisterOptionSection = () => {
    return (
        <section className="loginPage__register-text-section panel-wrapper__panel--image-background">
            <h2 className="panel-wrapper__title">New to Bookstore?</h2>
            <p className="panel-wrapper__paragraph">Sing up and discover a great amount of new opportunities!</p>
            <Link to='/register' className="panel-wrapper__link">SIGN UP</Link>
        </section>
    )
}
 
export default LogInPage;