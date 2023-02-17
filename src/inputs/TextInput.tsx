type TextInputProps = {
    state: string;
    setState: (val: string) => void;
    isRequired: boolean;
    placeholder: string;
  };

const TextInput = (props: TextInputProps) => {
    return (
        <input 
            type="text"
            required={props.isRequired}
            value={props.state} 
            onChange={(e) => {props.setState(e.target.value)}}
            placeholder={props.placeholder}
        />
    )
}
 
export default TextInput;