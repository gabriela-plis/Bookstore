import { useEffect, useState } from "react";
import User from "../../DTO/UserDTO";
import useFetch from "../../functions/useFetch";
import TextInput from "../../reusable-components/TextInput";
import PasswordInput from "../../reusable-components/PasswordInput";
import ResetPassword from "../../DTO/ResetPasswordDTO";



const Settings = () => {
    const userDTO = useFetch('http://localhost:8080/users/this') as unknown as User;

    const [user, setUser] = useState<User>({
        id: 0,
        firstName: "",
        lastName: "",
        phone: "",
        email: "",
        roles: []
    });
   
    const [readyToRender, setReadyToRender] = useState(false);

    useEffect( () => {
        if (Object.keys(userDTO).length !== 0) {
        setUser(userDTO);
        setReadyToRender(true);
        }

    },[userDTO])
    

    return (
        <section className="settings">
            <h2 className="settings__title">Account settings</h2>
            {readyToRender &&  
                <div className="settings__content">
                    <DataArea initialData={user.firstName} user={user} setUser={setUser} className="first-name" inputName="firstName" displayName="First name" />
                    <DataArea initialData={user.lastName} user={user} setUser={setUser} className="last-name" inputName="lastName" displayName="Last name" />
                    <DataArea initialData={user.email} user={user} setUser={setUser} className="email" displayName="Email" />
                    <DataArea initialData={user.phone} user={user} setUser={setUser} className="phone" displayName="Phone" />
                    <ResetPasswordArea />
                </div>     
            }
            <span className="settings__image"><div></div></span>
        </section>
    )
}

type DataAreaProps = {
    initialData: string;
    user: User;
    setUser: React.Dispatch<React.SetStateAction<User>>;
    className: string;
    displayName: string;
    inputName?: string;
    buttonName?: string;
}

const DataArea = (props: DataAreaProps) => {
    const {initialData, user, setUser, className, displayName, inputName, buttonName} = {...props}

    const[showEditPanel, setShowEditPanel] = useState(false);

    const handleClickEditBtn = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault();
        setShowEditPanel(true);
    }

    return (
        <div className = {"settings__data settings__data--"+className}>
            <label htmlFor={className}>
                <span className="settings__label-description">{displayName}:</span>
                {initialData ? (
                    <span>{initialData}</span>
                ) : (
                    <span>- - -</span>
                )} 
            </label>
            <button 
                className="btn btn--edit" 
                onClick={ (e) => {
                    handleClickEditBtn(e)
                }}
            >
               {buttonName ? (
                    <span>{buttonName}</span>
               ) : (
                    <span>Edit</span>
               )} 
            </button>
            {showEditPanel && (
                    <EditDataPanel 
                        initialData={initialData}
                        setShowEditPanel={setShowEditPanel}
                        user={user} 
                        setUser={setUser}
                        displayName={displayName} 
                        inputName={inputName ? inputName : className}
                    />
                )
            }
        </div>
    )
}

type EditDataPanelProps = {
    initialData: string;
    setShowEditPanel: React.Dispatch<React.SetStateAction<boolean>>;
    user: User;
    setUser: React.Dispatch<React.SetStateAction<User>>;
    displayName: string;
    inputName: string;
}

const EditDataPanel = (props: EditDataPanelProps) => {
    const {initialData, setShowEditPanel, user, setUser, displayName, inputName} = {...props}

    const [updatedData, setUpdatedData] = useState("")
    const [userToSend, setUserToSend] = useState<User>(user);

    const [error, setError] = useState(false);

    const handleEditData = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>, dataName: string) => {
        e.preventDefault();

        fetch('http://localhost:8080/users', {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify(userToSend)
            })
            .then(resp => {
                if (resp.status === 200) {
                    setError(false)
                    setUser(userToSend)
                    setShowEditData(false)
                }
            })
            .catch( error => {
                setError(true) 
            })

    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUpdatedData(e.target.value)
        setUserToSend(prevState => (
                    {
                        ...prevState,
                        [inputName]: e.target.value
                    }
                ))  
    }

    const handleCancelOperation = () => {
        setUpdatedData("")
        props.setShowEditPanel(false);
    } 

    return ( 
        <div className="edit__background">
            <section className="edit__content">
                <h2 className="edit__title">Edit {displayName}</h2>
                <div className="edit__input-container">
                    <label className="edit__label">{displayName}</label>
                    <TextInput 
                        name={inputName}
                        state={updatedData}
                        setState={handleChange} 
                        isRequired 
                        placeholder={initialData}

                    />
                </div>
                {error && <p className="incorrect-data-text">Something goes wrong, try again</p>}
                <button className="btn btn--cancel" onClick={handleCancelOperation}>Cancel</button>
                <button className="btn btn--save" 
                    onClick={ (e) => {
                        handleEditData(e, setShowEditPanel, inputName)                    
                    }}
                >Save</button>
            </section>
        </div>
     );
}



const ResetPasswordArea = () => {
    const[showEditPanel, setShowEditPanel] = useState(false)

    const handleResetOperation = () => {
        setShowEditPanel(true)
    }

    return (
        <div className="settings__data settings__data--password">
             <button className="btn" onClick={handleResetOperation}>Reset Password</button>
            {showEditPanel && (
                <ResetPasswordPanel
                    setShowEditPanel={setShowEditPanel}
                />
            )
            }
        </div> 
    )

}

type ResetPasswordPanelProps = {
    setShowEditPanel: React.Dispatch<React.SetStateAction<boolean>>;
}

const ResetPasswordPanel = (props: ResetPasswordPanelProps) => {
    const {setShowEditPanel} = {...props}

    const[passwords, setPasswords] = useState<ResetPassword>({
        currentPassword: "",
        newPassword: ""
    })

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPasswords(prevState => (
            {
                ...prevState,
                [e.target.name]: e.target.value
            }
        )) 

    }

    const handleResetPassword = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => {
        e.preventDefault();

       
        fetch('http://localhost:8080/users/password', {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify(passwords)
        })

        setShowEditData(false);
    }

    const handleCancelOperation = () => {
        setShowEditPanel(false);
    } 

    return ( 
        <div className="edit__background">
            <section className="edit__content">
                <h2 className="edit__title">Reset Password</h2>
                <div className="edit__input-container">
                    <label className="edit__label">Current Password</label>
                    <PasswordInput 
                        name="currentPassword"
                        state={passwords.currentPassword} 
                        setState={(e) => handleChange(e)} 
                    />
                    <label className="edit__label">New Password</label>
                    <PasswordInput
                        name="newPassword"
                        state={passwords.newPassword} 
                        setState={(e) => handleChange(e)} 
                    />
                </div>
                <button className="btn btn--cancel" onClick={handleCancelOperation}>Cancel</button>
                <button className="btn btn--save" 
                    onClick={ (e) => {
                        handleResetPassword(e, setShowEditPanel);
                    }}
                >Save</button>
            </section>
        </div>
     );
}

export default Settings;