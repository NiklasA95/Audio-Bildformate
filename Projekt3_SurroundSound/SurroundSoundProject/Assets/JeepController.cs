using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class JeepController : MonoBehaviour {

	new private Rigidbody rigidbody;
	private Vector3 start;
	private Vector3 finish;
	public float speed;
	private int direction = 1;

	// Use this for initialization
	void Start () {
		rigidbody = GetComponent<Rigidbody>();
		start = rigidbody.position;
		finish = new Vector3(start.x, start.y, -start.z);
	}
	
	// Update is called once per frame
	void Update () {
		if (rigidbody.position.z < start.z || rigidbody.position.z > finish.z) {
			direction *= -1;
		}

		rigidbody.transform.eulerAngles = new Vector3(0, direction < 0 ? 180 : 0, 0);
		rigidbody.velocity = new Vector3(0, 0, direction * speed);
	}
}
