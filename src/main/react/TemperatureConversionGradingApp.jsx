import React from "react";
import ReactDOM from 'react-dom';

class TemperatureConversionGradingApp extends React.Component {
    constructor(props){
        super(props);

        this.grade = this.grade.bind(this);
    }

    grade(){
        const element   =  ReactDOM.findDOMNode(this);

        var input_value         = element.querySelector("input[name=input_value]").value;
        var input_unit          = element.querySelector("input[name=input_unit]").value;
        var target_unit         = element.querySelector("input[name=target_unit]").value;
        var student_response    = element.querySelector("input[name=student_response]").value;

        if(
            input_value         === "" ||
            input_unit          === "" ||
            target_unit         === "" ||
            student_response    === ""
        ){
            console.log("Incomplete input, cancelling request to server");
            return;
        }

        var output_element = element.querySelector("td#output");

        var rounded_student_response    = Math.round(student_response*10)/10;

        console.log(
            "input_value=" + input_value
            + "&input_unit=" + input_unit
            + "&target_unit=" + target_unit
        );

        fetch(
            "http://localhost:8080/api/convert?"
            + "input_value=" + input_value
            + "&input_unit=" + input_unit
            + "&target_unit=" + target_unit
        ).then(
            response => response.text()
        ).then(
            authoritative_answer => {

                console.log("Authoritative answer: " + authoritative_answer + " (" + typeof authoritative_answer +")");
                console.log("Student (rounded): " + rounded_student_response + " (" + typeof rounded_student_response +")");


                output_element.innerHTML = (authoritative_answer === "invalid")?(
                    "invalid"
                ):((parseFloat(authoritative_answer) === rounded_student_response))?(
                    "correct"
                ):(
                    "incorrect"
                );

            }
        );
    }

    render() {
        return (
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>Input Numerical Value</th>
                            <th>Input Unit of Measure</th>
                            <th>Target Unit of Measure</th>
                            <th>Student Response</th>
                            <th>Output</th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr>
                            <td>
                                <input
                                    type="text"
                                    name="input_value"
                                    onBlur={this.grade}
                                />
                            </td>
                            <td>
                                <input
                                    type="text"
                                    name="input_unit"
                                    onBlur={this.grade}
                                />
                            </td>
                            <td>
                                <input
                                    type="text"
                                    name="target_unit"
                                    onBlur={this.grade}
                                />
                            </td>
                            <td>
                                <input
                                    type="text"
                                    name="student_response"
                                    onBlur={this.grade}
                                />
                            </td>
                            <td id="output">
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        );
    }
}

ReactDOM.render(
    <TemperatureConversionGradingApp />,
    document.getElementById('react-TemperatureConversionGradingApp')
);